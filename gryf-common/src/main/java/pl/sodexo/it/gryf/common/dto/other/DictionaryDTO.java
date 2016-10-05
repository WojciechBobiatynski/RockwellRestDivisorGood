package pl.sodexo.it.gryf.common.dto.other;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;

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
