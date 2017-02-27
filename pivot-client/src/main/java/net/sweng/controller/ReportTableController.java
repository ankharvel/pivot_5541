package net.sweng.controller;

import net.sweng.domain.GenericRow;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.apache.commons.lang3.tuple.Pair;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Date on 2/25/17.
 */
@ManagedBean
@SessionScoped
public class ReportTableController extends AbstractTableController {

    private static final String AGG_SUFFIX = "_agg";
    private static final String COL_SUFFIX = "_col";
    private static final String LB_ROW = "Row Labels";
    private static final String TOTAL = "Total";

    @ManagedProperty(value = "#{parameterReportController}")
    private ParameterReportController parameterController;

    private TreeNode rootNode;
    private boolean reportEnable;
    private String reportGenerated;

    @Override
    public void initialize() {
        createDynamicColumns();
    }

    @Override
    public void fillRecords(ActionEvent event) {
        try {
            ReportParameters parameters = parameterController.generateParameters();
            TableData data = pivotController.generateReportFromCSV(parameters);
            List<String> headers = obtainHeaders(data);
            setColumnKeys(headers.toArray(new String[headers.size()]));
            createDynamicColumns();
            setRootNode(obtainTree(data, headers));
            reportEnable = true;
            reportGenerated = parameters.getFileName();
        } catch (InvalidDataTypeException ex) {
            addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private TreeNode obtainTree(TableData td, List<String> headers) {
        TreeNode root = new DefaultTreeNode(new GenericRow(), null);
        if(headers.size() == 1) {
            GenericRow row = new GenericRow();
            row.put(headers.get(0), td.getData().get(0).get(td.getColumnNames()[0]));
            new DefaultTreeNode(row, root);
        }

        if( !obtainRows(td.getColumnNames()).isEmpty()) {
            TreeNodeContainer container = new TreeNodeContainer();
            TreeNode parent;
            String key;
            String aggregation = obtainAggregation(td.getColumnNames());
            GenericRow totalRow = new GenericRow();
            headers.stream().forEach(r -> totalRow.put(r, 0D));
            totalRow.put(LB_ROW, TOTAL);
            List<String> rawColumns = obtainColumns(td.getColumnNames());
            for(GenericRow inputRow: td.getData()) {
                parent = root;
                key = "";
                for(String rowLbl: obtainRows(td.getColumnNames())) {
                    key = key + "-" + inputRow.get(rowLbl);
                    Pair<TreeNode, GenericRow> pairRow = container.getRow(key, parent);
                    for(String header: headers) {
                        addRowInformation(inputRow, pairRow.getValue(), header, rowLbl, aggregation, rawColumns);
                        if(!rawColumns.isEmpty() && parent == root) {
                            incrementTotalRow(inputRow, totalRow, header, aggregation, rawColumns);
                        }
                    }
                    parent = pairRow.getKey();
                }
                if(rawColumns.isEmpty()) {
                    incrementTotalRow(inputRow, totalRow, aggregation);
                }
            }
            new DefaultTreeNode(totalRow, root);
        }


        return root;
    }

    private static void addRowInformation(GenericRow inputRow, GenericRow outRow, String header,
                                          String rowLbl, String aggregation, List<String> rawColumns) {
        if(header.equalsIgnoreCase(LB_ROW)) {
            outRow.put(header, inputRow.get(rowLbl));
        } else if(header.equalsIgnoreCase(aggregation)) {
            if (outRow.get(header) == null) {
                outRow.put(header, inputRow.get(header + AGG_SUFFIX));
            } else {
                Double val1 = Double.valueOf((String) inputRow.get(header + AGG_SUFFIX));
                Double val2 = Double.valueOf(outRow.get(header).toString());
                outRow.put(header, val1 + val2);
            }
        } else {
            for(String col: rawColumns) {
                if(!header.equalsIgnoreCase((String)inputRow.get(col + COL_SUFFIX))) return;
            }
            if (outRow.get(header) == null) {
                outRow.put(header, inputRow.get(aggregation + AGG_SUFFIX));
            } else {
                Double val1 = Double.valueOf((String) inputRow.get(aggregation + AGG_SUFFIX));
                Double val2 = Double.valueOf(outRow.get(header).toString());
                outRow.put(header, val1 + val2);
            }
        }
    }

    private static void incrementTotalRow(GenericRow inputRow, GenericRow totalRow, String aggregation) {
        Double val1 = (Double) totalRow.get(aggregation);
        Double val2 = Double.valueOf((String) inputRow.get(aggregation + AGG_SUFFIX));
        totalRow.put(aggregation, val1 + val2);
    }

    private static void incrementTotalRow(GenericRow inputRow, GenericRow totalRow, String header,
                                          String aggregation, List<String> rawColumns) {
        for(String col: rawColumns) {
            if(header.equalsIgnoreCase((String)inputRow.get(col + COL_SUFFIX))) {
                Double val1 = (Double) totalRow.get(header);
                Double val2 = Double.valueOf((String) inputRow.get(aggregation + AGG_SUFFIX));
                totalRow.put(header, val1 + val2);
            }
        }
    }

    private static List<String> obtainHeaders(TableData data) {
        String aggLbl = obtainAggregation(data.getColumnNames());
        List<String> headers = new ArrayList<>();
        if(obtainRows(data.getColumnNames()).isEmpty()) {
            headers.add(aggLbl);
        } else {
            headers.add(LB_ROW);
        }

        List<String> rawColumns = obtainColumns(data.getColumnNames());
        if(!rawColumns.isEmpty()) {
            rawColumns.stream().forEach(lbl -> headers.addAll(data.getPivotColumnValues(lbl)));
        } else if(!headers.contains(aggLbl)) {
            headers.add(aggLbl);
        }

        return headers;
    }

    private static String obtainAggregation(String[] headers) {
        for(String s: headers) {
            if(s.endsWith(AGG_SUFFIX)) return s.replace(AGG_SUFFIX, "");
        }
        //TODO: Fix exception
        throw new RuntimeException();
    }

    private static List<String> obtainRows(String[] headers) {
        return Arrays.asList(headers).stream()
                .filter(s -> !(s.endsWith(AGG_SUFFIX) || s.endsWith(COL_SUFFIX)))
                .collect(Collectors.toList());
    }

    private static List<String> obtainColumns(String[] headers) {
        return Arrays.asList(headers).stream()
                .filter(s -> s.endsWith(COL_SUFFIX)).map(ss -> ss.replace(COL_SUFFIX, ""))
                .collect(Collectors.toList());
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public boolean isReportEnable() {
        return reportEnable;
    }

    public String getReportGenerated() {
        return reportGenerated;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public void setParameterController(ParameterReportController parameterController) {
        this.parameterController = parameterController;
    }

}

class TreeNodeContainer {

    private Map<String, Pair<TreeNode, GenericRow>> treeNodeMap;

    public TreeNodeContainer() {
        this.treeNodeMap = new HashMap<>();
    }

    public Pair<TreeNode, GenericRow> getRow(String key, TreeNode parent) {
        Pair<TreeNode, GenericRow> pair = treeNodeMap.get(key);
        if(pair == null) {
            GenericRow row = new GenericRow();
            pair = Pair.of(new DefaultTreeNode(row, parent), row);
            treeNodeMap.put(key, pair);
        }
        return pair;
    }

}