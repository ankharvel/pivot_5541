package net.sweng.config;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

import static net.sweng.config.SessionKeys.ACTIVE_SESSION_PREFIX;

/**
 * Date on 2/14/17.
 */
public class HttpSessionHandler implements HttpSessionListener {

    private static final AtomicInteger seq = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent ev) {
        ev.getSession().setAttribute(ACTIVE_SESSION_PREFIX, seq.incrementAndGet());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent ev) {

    }

    public static void putSessionAttribute(String key, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.setAttribute(key, value);
        System.out.println(session.getAttribute(key));
    }

    public static Object getSessionAttribute(String key) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return session.getAttribute(key);
    }

}
