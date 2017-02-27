package net.sweng.config;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

import static net.sweng.config.SessionKeys.*;

/**
 * Date on 2/14/17.
 */
public class HttpSessionHandler implements HttpSessionListener {

    private static final AtomicInteger seq = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent ev) {
        ServletContext ctx = ev.getSession().getServletContext();
        ev.getSession().setAttribute(ACTIVE_SESSION_PREFIX, seq.incrementAndGet());
        ev.getSession().setAttribute(FILE_AVAILABLE, false);
        ev.getSession().setAttribute(SCHEMA_AVAILABLE, false);
        ev.getSession().setAttribute(TEMP_FOLDER_PATH, ctx.getRealPath("/WEB-INF/tmp/" + seq.get()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent ev) {

    }

    public static void putSessionAttribute(String key, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.setAttribute(key, value);
    }

    public static <T> T getSessionAttribute(String key, Class<T> clazz) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return  clazz.cast(session.getAttribute(key));
    }

}
