package pl.sodexo.it.gryf.model.api;

import pl.sodexo.it.gryf.common.utils.GryfUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Isolution on 2016-11-29.
 */
@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean b) {
        return GryfUtils.convertBooleanToDatabaseColumn(b);
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        return  GryfUtils.convertDatabaseColumnToBoolean(s);
    }
}
