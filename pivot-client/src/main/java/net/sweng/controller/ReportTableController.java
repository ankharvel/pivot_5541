package net.sweng.controller;

import net.sweng.domain.GenericRow;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
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
    private static final String LB_EMPTY = "-";

    @ManagedProperty(value = "#{parameterReportController}")
    private ParameterReportController parameterController;

    private TreeNode rootNode;
    private boolean reportEnable;
    private String reportGenerated;
    
    private String lblTotal;
    private String dynamicWidth;

    private List<String> filterValues;
    private String selectedFilterValue;

    @Override
    public void initialize() {
        createDynamicColumns();
        lblTotal = bundle.getString("lbl_total");
    }

    @Override
    public void fillRecords(FacesEvent event) {
        try {
            if(event instanceof ValueChangeEvent)  {
                String value = ((ValueChangeEvent) event).getNewValue().toString();
                if(value.contains(ReportParameters.class.getSimpleName())) {
                    parameterController.setSelectedParameters(((ValueChangeEvent) event).getNewValue());
                    selectedFilterValue = "";
                } else {
                    selectedFilterValue = value;
                }
            }
            ReportParameters parameters = parameterController.generateParameters();
            if(StringUtils.isNotBlank(selectedFilterValue)) {
                parameters.setFilterValues(Collections.singletonList(selectedFilterValue));
            } else {
                parameters.setFilterValues(new ArrayList<>());
            }
            TableData data = pivotController.generateReportFromCSV(parameters);
            if(!parameters.getReportFilter().isEmpty()) {
                filterValues = data.getColumnValues(parameters.getReportFilter().iterator().next().getColumnName());
            }
            List<String> headers = obtainHeaders(data);
            setColumnKeys(headers.toArray(new String[headers.size()]));
            updateDynamicWidth(headers.size());
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

    public void addParametersAndFillRecords(FacesEvent event) {
        parameterController.addParameters();
        selectedFilterValue = "";
        fillRecords(event);
    }

    private TreeNode obtainTree(TableData td, List<String> headers) {
        TreeNode root = new DefaultTreeNode(new GenericRow(), null);
        root.setExpanded(true);
        if(headers.size() == 1) {
            GenericRow row = new GenericRow();
            row.put(headers.get(0), td.getData().get(0).get(td.getColumnNames()[0]));
            new DefaultTreeNode(row, root);
            return root;
        }

        TreeNodeContainer container = new TreeNodeContainer();
        TreeNode parent;
        String key;
        String aggregation = obtainAggregation(td.getColumnNames());
        GenericRow totalRow = new GenericRow();
        headers.stream().filter(s -> !(obtainRows(td.getColumnNames()).contains(s))).forEach(r -> totalRow.put(r, 0D));
        if(!obtainRows(td.getColumnNames()).isEmpty()) {
            totalRow.put(obtainRows(td.getColumnNames()).iterator().next(), lblTotal);
        }
        totalRow.put(LB_EMPTY, aggregation);
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
            if(!rawColumns.isEmpty() && obtainRows(td.getColumnNames()).isEmpty()) {
                for(String header: headers) {
                    addRowInformation(inputRow, totalRow, header, null, aggregation, rawColumns);
                }
            }
            if(rawColumns.isEmpty()) {
                incrementTotalRow(inputRow, totalRow, aggregation);
            }
        }
        new DefaultTreeNode(totalRow, root);

        return root;
    }

    private static void addRowInformation(GenericRow inputRow, GenericRow outRow, String header,
                                          String rowLbl, String aggregation, List<String> rawColumns) {
        if(header.equalsIgnoreCase(rowLbl)) {
            outRow.put(header, inputRow.get(rowLbl));
        } else if(header.equalsIgnoreCase(aggregation)) {
            if (outRow.get(header) == null) {
                outRow.put(header, inputRow.get(header + AGG_SUFFIX));
            } else {
                Double val1 = Double.valueOf((String) inputRow.get(header + AGG_SUFFIX));
                Double val2 = Double.valueOf(outRow.get(header).toString());
                outRow.put(header, val1 + val2);
            }
        } else if (!rawColumns.isEmpty()) {
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
        List<String> rawColumns = obtainColumns(data.getColumnNames());
        List<String> headers = new ArrayList<>();
        if(obtainRows(data.getColumnNames()).isEmpty() && rawColumns.isEmpty()) {
            headers.add(aggLbl);
        } else if(!obtainRows(data.getColumnNames()).isEmpty() && rawColumns.isEmpty()) {
            headers.addAll(obtainRows(data.getColumnNames()));
            headers.add(aggLbl);
        } else if(obtainRows(data.getColumnNames()).isEmpty() && !rawColumns.isEmpty()) {
            headers.add(LB_EMPTY);
            rawColumns.stream().forEach(lbl -> headers.addAll(data.getColumnValues(lbl)));
        } else {
            headers.addAll(obtainRows(data.getColumnNames()));
            rawColumns.stream().forEach(lbl -> headers.addAll(data.getColumnValues(lbl)));
        }

        return headers;
    }

    private static String obtainAggregation(String[] headers) {
        for(String s: headers) {
            if(s.endsWith(AGG_SUFFIX)) return s.replace(AGG_SUFFIX, "");
        }
        return "";
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

    private void updateDynamicWidth(int size) {
        dynamicWidth = size <= 6 ? String.valueOf(size * 140) + "px" : "100%";
    }

    public String getDynamicWidth() {
        return dynamicWidth;
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

    public List<String> getFilterValues() {
        return filterValues;
    }

    public String getSelectedFilterValue() {
        return selectedFilterValue;
    }

    public void setSelectedFilterValue(String selectedFilterValue) {
        this.selectedFilterValue = selectedFilterValue;
    }

    public void setParameterController(ParameterReportController parameterController) {
        this.parameterController = parameterController;
    }

    public String formatField(Object o) {
        if(o != null) {
            String val = o.toString();
            if(val.matches("^\\-?\\d+\\.\\d+$")) {
                return String.valueOf((double)Math.round(Double.valueOf(val)*100)/100);
            }
            return val;
        } else {
            return "";
        }
    }

    private class TreeNodeContainer {

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
                pair.getKey().setExpanded(true);
            }
            return pair;
        }

    }

}