package net.sweng.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UploadedFilesTableController extends AbstractTableController implements Serializable {

    @ManagedProperty(value = "#{fileUploadController}")
    private FileUploadController fileUploadBean;

    @ManagedProperty(value = "#{msg.uploaded_files}")
    private String columnName;

    private Map<String, Object> selectedFile;

    public void setFileUploadBean(FileUploadController fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public void initialize() {
        setColumnKeys(new String[]{columnName});
        createDynamicColumns();
    }

    public void fillRecords(ActionEvent event) {
        List<Map<String, Object>> data = new LinkedList<>();
        fileUploadBean.getUploadedFiles().stream().forEach(c -> data.add(Collections.singletonMap(columnName, c)));
        setRegisters(data);
    }

    public Map<String, Object> getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(Map<String, Object> selectedFile) {
        this.selectedFile = selectedFile;
    }

}
