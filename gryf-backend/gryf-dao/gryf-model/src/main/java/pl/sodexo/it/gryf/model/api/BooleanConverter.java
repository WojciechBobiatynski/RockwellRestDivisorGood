package pl.sodexo.it.gryf.model.api;

import com.google.common.base.Strings;
import pl.sodexo.it.gryf.common.utils.GryfConstants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Isolution on 2016-11-29.
 */
@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean b) {
        return (b != null && b) ? GryfConstants.FLAG_TRUE : GryfConstants.FLAG_FALSE;
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        return GryfConstants.FLAG_TRUE.equals(s);
    }
}
