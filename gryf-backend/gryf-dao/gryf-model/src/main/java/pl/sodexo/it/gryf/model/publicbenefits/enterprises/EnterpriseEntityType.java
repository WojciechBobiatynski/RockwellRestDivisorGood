package pl.sodexo.it.gryf.model.publicbenefits.enterprises;

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
@Table(name = "ENTERPRISE_ENTITY_TYPES", schema = "APP_PBE")
public class EnterpriseEntityType extends GryfEntity implements DictionaryEntity {

    //STATIC FIELDS - ATRIBUTES

    public static final String NAME_ATTR_NAME = "name";
    public static final String ACTIVE_ATTR_NAME = "active";

    //FIELDS

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private Boolean active;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    //DICTINARY METHODS

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
        return ACTIVE_ATTR_NAME;
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
        return Objects.equals(id, ((EnterpriseEntityType) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
