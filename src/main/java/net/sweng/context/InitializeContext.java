package net.sweng.context;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.util.logging.Logger;

/**
 * This class is executed once the feces context is initialized and delete all temporal files
 * Created by oscar on 1/23/17.
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class InitializeContext {

    private static final Logger logger = Logger.getLogger(InitializeContext.class.getName());

    @PostConstruct
    private void removeTemps() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String tmpPath = ctx.getRealPath("/WEB-INF/tmp");
        try {
            File tmp = new File(tmpPath);
            if(tmp.isDirectory()) {
                File[] files = tmp.listFiles();
                for(File child: files) {
                    if(child.isDirectory()) {
                        removeDir(child);
                    } else {
                        child.delete();
                    }
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removeDir(File dir) {
        File[] files = dir.listFiles();
        if(files != null) {
            for(File f: files) {
                f.delete();
            }
        }
        dir.delete();
    }

}
