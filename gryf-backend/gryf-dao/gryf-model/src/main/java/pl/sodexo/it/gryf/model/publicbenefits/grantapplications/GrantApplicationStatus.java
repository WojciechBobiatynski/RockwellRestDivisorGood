package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString
@Entity
@Table(name = "GRANT_APPLICATION_STATUSES", schema = "APP_PBE")
public class GrantApplicationStatus extends GryfEntity implements DictionaryEntity {

    //STATIC FEILDS - STATUSES CODE

    public static final String NEW_CODE = "NEW";
    public static final String APPLIED_CODE = "APPLIED";
    public static final String EXECUTED_CODE = "EXECUTED";
    public static final String REJECTED_CODE = "REJECTED";

    //STATIC FIELDS - ATRIBUTES

    public static final String NAME_ATTR_NAME = "name";

    //FIELDS

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //DICTIONARY METHODS

    public Object getDictionaryId() {
        return id;
    }

    public String getDictionaryName() {
        return name;
    }

    public String getOrderField(){
        return NAME_ATTR_NAME;
    }

    public String getActiveField() {
        return null;
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
        return Objects.equals(id, ((GrantApplicationStatus) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
