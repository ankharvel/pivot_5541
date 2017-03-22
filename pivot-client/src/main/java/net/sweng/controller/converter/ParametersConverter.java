package net.sweng.controller.converter;

import net.sweng.controller.ParameterReportController;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.ResourceBundle;

/**
 * @author oscar on 3/21/17.
 */
@FacesConverter("parameterConverter")
public class ParametersConverter implements Converter {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) throws ConverterException {
        try {
            ParameterReportController controller = (ParameterReportController) ctx.getExternalContext().getSessionMap().get("parameterReportController");
            return controller.getParametersList().stream().filter(p -> p.toString().equalsIgnoreCase(value)).findFirst().get();
        } catch (Exception ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("err_conversion"), bundle.getString("err_invalid_parameter")));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        if(value != null) {
            return String.valueOf(value);
        }
        else {
            return null;
        }
    }
}
