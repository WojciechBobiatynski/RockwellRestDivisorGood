package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = "application")
@Entity
@Table(name = "GRANT_APPLICATION_FORM_DATA", schema = "APP_PBE")
public class GrantApplicationFormData extends AuditableEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @JsonBackReference("formData")
    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable=false, updatable=false)
    private GrantApplication application;

    @Lob
    @Column(name = "FORM_DATA")
    private String formData;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantApplication getApplication() {
        return application;
    }

    public void setApplication(GrantApplication grantApplication) {
        this.application = grantApplication;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
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
        return Objects.equals(id, ((GrantApplicationFormData) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
