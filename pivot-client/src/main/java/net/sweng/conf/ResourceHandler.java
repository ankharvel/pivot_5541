package net.sweng.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.logging.Logger;

/**
 * This class is executed once the context is initialized and delete all temporal files
 * Created by oscar on 1/23/17.
 */
@Component
public class ResourceHandler {

    private static final Logger logger = Logger.getLogger(ResourceHandler.class.getName());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void removeTemps() {
        Resource tmpPath = applicationContext.getResource("/WEB-INF/tmp");
        try {
            File tmp = tmpPath.getFile();
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
