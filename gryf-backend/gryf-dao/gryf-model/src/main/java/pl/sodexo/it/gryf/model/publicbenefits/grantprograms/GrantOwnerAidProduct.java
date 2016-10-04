/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import lombok.ToString;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"grantOwner", "reimbursementTrainings"})
@Entity
@Table(name = "GRANT_OWNER_AID_PRODUCTS", schema = "APP_PBE")
public class GrantOwnerAidProduct extends GryfEntity implements DictionaryEntity{

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    private String id;

    @JoinColumn(name = "GRANT_OWNER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GrantOwner grantOwner;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;

    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "grantOwnerAidProduct")
    private List<ReimbursementTraining> reimbursementTrainings;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GrantOwner getGrantOwner() {
        return grantOwner;
    }

    public void setGrantOwner(GrantOwner grantOwner) {
        this.grantOwner = grantOwner;
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

    //LIST METHODS

    private List<ReimbursementTraining> getInitializedReimbursementTraining() {
        if (reimbursementTrainings == null)
            reimbursementTrainings = new ArrayList<>();
        return reimbursementTrainings;
    }

    public List<ReimbursementTraining> getReimbursementTraining() {
        return Collections.unmodifiableList(getInitializedReimbursementTraining());
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
        return Objects.equals(id, ((GrantOwnerAidProduct) o).id);
    }
}
