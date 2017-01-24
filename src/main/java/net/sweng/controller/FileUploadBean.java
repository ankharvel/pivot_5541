package net.sweng.controller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.util.logging.Logger;

@ManagedBean
@SessionScoped
public class FileUploadBean {

    private static final Logger logger = Logger.getLogger(FileUploadBean.class.getName());

    /**
     * This method runs when the file is uploaded and store a copy in a tmp folder
     * using the current session Id as relative path
     * @param e file upload event
     */
    public void fileUploadListener(FileUploadEvent e){
        // Get uploaded file from the FileUploadEvent
        UploadedFile file = e.getFile();
        try {
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            String destPath = ctx.getRealPath("/WEB-INF/tmp/" + ctx.getSessionId(false));
            File destDir = new File(destPath);
            if(!destDir.exists()) {
                destDir.mkdirs();
            }
            file.write(destDir.getPath() + "/" + file.getFileName());
            logger.info(String.format("Uploaded file: %s, Size : %s", file.getFileName(), file.getSize()));
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

}