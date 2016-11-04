package pl.sodexo.it.gryf.model.publicbenefits.contracts;

import pl.sodexo.it.gryf.model.api.AuditableEntity;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Isolution on 2016-10-28.
 */
@Entity
@Table(name = "CONTRACT_TYPES", schema = "APP_PBE")
public class ContractType implements DictionaryEntity {

    //STATIC FIELDS - ATRIBUTES
    public static final String NAME_ATTR_NAME = "name";

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDINAL")
    private Integer ordinal;

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
        return "ordinal";
    }

    @Override
    public String getActiveField() {
        return null;
    }
}
