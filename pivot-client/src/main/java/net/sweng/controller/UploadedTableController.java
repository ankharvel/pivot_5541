package net.sweng.controller;

import net.sweng.domain.GenericRow;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static net.sweng.config.HttpSessionHandler.putSessionAttribute;
import static net.sweng.config.SessionKeys.FILE_AVAILABLE;

@ManagedBean
@SessionScoped
public class UploadedTableController extends AbstractTableController implements Serializable {

    private static final Logger logger = Logger.getGlobal();

    private GenericRow selectedFile;

    private Set<String> uploadedFiles = new HashSet<>();

    @Override
    public void initialize() {
        setColumnKeys(new String[]{bundle.getString("header_uploaded_files")});
        createDynamicColumns();
    }

    @Override
    public void fillRecords(ActionEvent event) {
        List<GenericRow> data = new LinkedList<>();
        String columnName = bundle.getString("header_uploaded_files");
        uploadedFiles.stream().forEach(c -> data.add(GenericRow.createSingleton(columnName, c)));
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
            putSessionAttribute(FILE_AVAILABLE, true);
            logger.info(String.format("Uploaded file: %s, Size : %s", file.getFileName(), file.getSize()));
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    //-----------------------  GETTERS & SETTERS ---------------------------------

    public Map<String, Object> getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(GenericRow selectedFile) {
        this.selectedFile = selectedFile;
    }

    public Set<String> getUploadedFiles() {
        return uploadedFiles;
    }

}