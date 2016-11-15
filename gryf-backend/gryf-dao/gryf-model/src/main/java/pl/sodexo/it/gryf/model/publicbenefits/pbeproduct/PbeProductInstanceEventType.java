package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-03.
 */
@Entity
@ToString
@Table(name = "PBE_PRODUCT_INSTANCE_E_T", schema = "APP_PBE")
public class PbeProductInstanceEventType extends GryfEntity implements DictionaryEntity {

    //STATIC FEILDS - TYPE CODE

    public static final String NEW_CODE = "NEW";
    public static final String RES_CODE = "RES";
    public static final String USE_CODE = "USE";
    public static final String REIMB_CODE = "REIMB";

    //FIELDS

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    //DICTIONARY METHODS

    public Object getDictionaryId() {
        return id;
    }

    public String getDictionaryName() {
        return name;
    }

    public String getOrderField(){
        return "name";
    }

    public String getActiveField() {
        return null;
    }

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

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
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
        return Objects.equals(id, ((PbeProductInstanceEventType) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
