package pl.sodexo.it.gryf.common.dto;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.DictionaryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@ToString
public class DictionaryDTO extends GryfDto {

    //FIELDS

    private Object id;

    private String name;

    //CONSTRUCTORS

    public DictionaryDTO() {
    }

    public DictionaryDTO(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    protected DictionaryDTO(DictionaryEntity entity) {
        this(entity.getDictionaryId(), entity.getDictionaryName());
    }

    //STATIC METHODS - CREATE

    public static DictionaryDTO create(DictionaryEntity entity) {
        return entity != null ? new DictionaryDTO(entity) : null;
    }

    //GETETRS & SETERS

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
