package net.sweng.controller.converter;

import net.sweng.controller.MainPanelController;
import net.sweng.domain.SourceDetail;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.ResourceBundle;

/**
 * Created on 3/27/17.
 */
@FacesConverter("sourceDetailConverter")
public class SourceDetailConverter implements Converter {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) throws ConverterException {
        try {
            if(value == null) return new SourceDetail("", null);
            MainPanelController controller = (MainPanelController) ctx.getExternalContext().getSessionMap().get("mainPanelController");
            return controller.getSourceDetailList().stream().filter(d -> d.toString().equalsIgnoreCase(value)).findFirst().get();
        } catch (Exception ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("err_conversion"), bundle.getString("err_invalid_source_detail")));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        if(value != null && value instanceof SourceDetail) {
            SourceDetail detail = (SourceDetail) value;
            return detail.toString();
        }
        return "";
    }
}
