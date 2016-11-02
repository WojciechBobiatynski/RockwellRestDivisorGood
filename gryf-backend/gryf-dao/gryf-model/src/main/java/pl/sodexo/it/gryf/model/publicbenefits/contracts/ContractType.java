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
public class ContractType extends AuditableEntity implements DictionaryEntity {

    //STATIC FIELDS - ATRIBUTES
    public static final String DESC_ATTR_NAME = "description";

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return description;
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
