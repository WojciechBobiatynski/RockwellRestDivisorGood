/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@Entity
@Table(name = "REIMBURSEMENT_ATTACHMENT_TYPES", schema = "APP_PBE")
public class ReimbursementAttachmentType extends GryfEntity implements DictionaryEntity {

    //STATIC FEILDS - STATUSES CODE
    public static final String ORIGINAL_CODE = "ORIGINAL";

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    private String id;

    @Size(max = 30)
    @Column(name = "NAME")
    private String name;

    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "name";
    }

    @Override
    public String getActiveField() {
        return null;
    }


    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((ReimbursementAttachmentType) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachmentTypes[ id=" + id + " ]";
    }
    
}
