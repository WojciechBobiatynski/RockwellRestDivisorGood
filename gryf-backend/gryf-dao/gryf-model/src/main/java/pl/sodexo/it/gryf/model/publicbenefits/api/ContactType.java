package pl.sodexo.it.gryf.model.publicbenefits.api;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-19.
 */
@ToString
@Entity
@Table(name = "CONTACT_TYPES", schema = "APP_PBE")
public class ContactType extends GryfEntity {

    //STATIC FIELDS - TYPE

    public static final String TYPE_PHONE = "PHONE";
    public static final String TYPE_EMAIL = "EMAIL";
    public static final String TYPE_VER_EMAIL = "VER_EMAIL";

    //STATIC FIELDS - ATTRIBUTE NAME

    public static final String TYPE_ATTR_NAME = "type";
    public static final String NAME_ATTR_NAME = "name";
    public static final String VALIDATION_CLASS_ATTR_NAME = "validationClass";

    //FIELDS

    @Id
    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALIDATION_CLASS")
    private String validationClass;

    //FIELDS

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidationClass() {
        return validationClass;
    }

    public void setValidationClass(String validationClass) {
        this.validationClass = validationClass;
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(type, ((ContactType) o).type);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type.hashCode()) % 102;
    }

}
