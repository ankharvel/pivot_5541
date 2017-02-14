package net.sweng.config;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date on 2/14/17.
 */
public class HttpSessionHandler implements HttpSessionListener {

    private static final Map<String, Object> staticConfiguration = new HashMap<>();
    private static final AtomicInteger seq = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent ev) {
        registerSession(ev.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent ev) {
        staticConfiguration.remove(ev.getSession().getId());
    }

    private void registerSession(String sessionId) {
        staticConfiguration.put(sessionId, seq.incrementAndGet());
    }

    public static int getMappedSessionId() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        return (int) staticConfiguration.get(ctx.getSessionId(false));
    }

}
