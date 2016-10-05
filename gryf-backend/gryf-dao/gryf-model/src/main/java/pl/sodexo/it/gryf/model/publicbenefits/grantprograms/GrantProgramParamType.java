package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = "params")
@Entity
@Table(name = "GRANT_PROGRAM_PARAM_TYPES", schema = "APP_PBE")
public class GrantProgramParamType extends GryfEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPRTION")
    private String description;

    @JsonManagedReference("params")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paramType")
    private List<GrantProgramParam> params;

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

    //LIST METHODS

    private List<GrantProgramParam> getInitializedParams() {
        if (params == null)
            params = new ArrayList<>();
        return params;
    }

    public List<GrantProgramParam> getParams() {
        return Collections.unmodifiableList(getInitializedParams());
    }

    public void addParam(GrantProgramParam param) {
        if (param.getParamType() != null && param.getParamType() != this) {
            param.getParamType().getInitializedParams().remove(param);
        }
        if (param.getId() == null || !getInitializedParams().contains(param)) {
            getInitializedParams().add(param);
        }
        param.setParamType(this);
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
        return Objects.equals(id, ((GrantProgramParamType) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
