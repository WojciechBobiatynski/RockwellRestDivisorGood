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
@Table(name = "ENTERPRISE_SIZES", schema = "APP_PBE")
public class EnterpriseSize extends GryfEntity implements DictionaryEntity {

    //STATIC FIELDS - ATRIBUTES

    public static final String SIZE_TO_ATTR_NAME = "sizeTo";

    //FIELDS

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SIZE_FROM")
    private Integer sizeFrom;

    @Column(name = "SIZE_TO")
    private Integer sizeTo;

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

    public Integer getSizeFrom() {
        return sizeFrom;
    }

    public void setSizeFrom(Integer sizeFrom) {
        this.sizeFrom = sizeFrom;
    }

    public Integer getSizeTo() {
        return sizeTo;
    }

    public void setSizeTo(Integer sizeTo) {
        this.sizeTo = sizeTo;
    }

    //DICTIONARY METHODS

    @Override
    public Object getDictionaryId() {
        return id;
    }

    @Override
    public String getDictionaryName() {
        return name;
    }

    @Override
    public String getOrderField() {
        return SIZE_TO_ATTR_NAME;
    }

    @Override
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
        return Objects.equals(id, ((EnterpriseSize) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
