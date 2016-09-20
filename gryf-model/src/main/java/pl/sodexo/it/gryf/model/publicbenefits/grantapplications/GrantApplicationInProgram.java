package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.model.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@Entity
@Table(name = "GRANT_APPLICATIONS_IN_PROGRAMS", schema = "APP_PBE")
public class GrantApplicationInProgram extends GryfEntity {

    //FIELDS

    @EmbeddedId
    protected GrantApplicationInProgramPK id;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @ManyToOne
    @JsonBackReference("applicationsInProgram")
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "ID", insertable=false, updatable=false)
    private GrantApplicationVersion applicationVersion;

    @JsonBackReference("applicationInPrograms")
    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID", insertable=false, updatable=false)
    private GrantProgram grantProgram;

    //GETTERS & SETTERS

    public GrantApplicationInProgramPK getId() {
        return id;
    }

    public void setId(GrantApplicationInProgramPK id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public GrantApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(GrantApplicationVersion grantApplicationVersion) {
        this.applicationVersion = grantApplicationVersion;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
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
        return Objects.equals(id, ((GrantApplicationInProgram) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
