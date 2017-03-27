package net.sweng.controller.converter;

import net.sweng.domain.DatabaseType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created on 3/25/17.
 */
@FacesConverter("databaseTypeConverter")
public class DatabaseTypeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        return DatabaseType.getDatabaseType(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        if(value != null) {
            DatabaseType type = DatabaseType.getDatabaseType(String.valueOf(value));
            if(type != null) return type.name();
        }
        return "";
    }

}
