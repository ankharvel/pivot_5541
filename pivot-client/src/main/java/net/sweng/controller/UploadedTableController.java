package net.sweng.controller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

@ManagedBean
@ViewScoped
public class UploadedTableController extends AbstractTableController implements Serializable {

    private static final Logger logger = Logger.getGlobal();

    @ManagedProperty(value = "#{msg.uploaded_files}")
    private String columnName;

    private Map<String, Object> selectedFile;

    private Set<String> uploadedFiles = new HashSet<>();

    @Override
    public void initialize() {
        setColumnKeys(new String[]{columnName});
        createDynamicColumns();
    }

    @Override
    public void fillRecords(ActionEvent event) {
        List<Map<String, Object>> data = new LinkedList<>();
        uploadedFiles.stream().forEach(c -> data.add(Collections.singletonMap(columnName, c)));
        setRegisters(data);
    }

    /**
     * This method runs when the file is uploaded and store a copy in a tmp folder
     * using the current session Id as relative path
     * @param e file upload event
     */
    public void fileUploadListener(FileUploadEvent e){
        UploadedFile file = e.getFile();
        try {
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            String destPath = ctx.getRealPath("/WEB-INF/tmp/" + ctx.getSessionId(false));
            File destDir = new File(destPath);
            if(!destDir.exists()) {
                destDir.mkdirs();
            }
            file.write(destDir.getPath() + "/" + file.getFileName());
            uploadedFiles.add(file.getFileName());
            logger.info(String.format("Uploaded file: %s, Size : %s", file.getFileName(), file.getSize()));
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    //-----------------------  GETTERS & SETTERS ---------------------------------

    public Map<String, Object> getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(Map<String, Object> selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Set<String> getUploadedFiles() {
        return uploadedFiles;
    }

}