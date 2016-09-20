package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.model.AuditableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@Entity
@Table(name = "GRANT_APPLICATION_PKD", schema = "APP_PBE")
public class GrantApplicationPkd extends AuditableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JsonBackReference("pkdList")
    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", referencedColumnName = "ID")
    private GrantApplicationBasicData applicationBasic;

    @Column(name = "PKD_CODE")
    private String pkdCode;

    //GETETRS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantApplicationBasicData getApplicationBasic() {
        return applicationBasic;
    }

    public void setApplicationBasic(GrantApplicationBasicData applicationId) {
        this.applicationBasic = applicationId;
    }

    public String getPkdCode() {
        return pkdCode;
    }

    public void setPkdCode(String pkdCode) {
        this.pkdCode = pkdCode;
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
        return Objects.equals(id, ((GrantApplicationPkd) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
