package net.sweng.controller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class FileUploadBean {

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void fileUploadListener(FileUploadEvent e){
        // Get uploaded file from the FileUploadEvent
        this.file = e.getFile();
        String cons = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/" + file.getFileName());
        System.out.println(cons);
        try {
            file.write(cons);
        } catch (Exception e1) {
            System.out.println("Pailas");
        }
        // Print out the information of the file
        System.out.println("Uploaded File Name Is// :: "+file.getFileName()+" :: Uploaded File Size :: "+file.getSize());
    }

    public String dummyAction(){
        System.out.println("Uploaded File Name Is :: "+file.getFileName()+" :: Uploaded File Size :: "+file.getSize());
        return "";
    }

}