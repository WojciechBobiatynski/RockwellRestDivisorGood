package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = {"grantProgram", "paramType"})
@Entity
@Table(name = "GRANT_PROGRAM_PARAMS", schema = "APP_PBE")
@NamedQueries(
        {@NamedQuery(name = GrantProgramParam.FIND_BY_GRANT_PROGRAM_IN_DATE, query="select distinct gpp from GrantProgramParam gpp " +
                                                                            "where gpp.grantProgram.id = :grantProgramId " +
                                                                            "and gpp.paramType.id = :paramTypeId " +
                                                                            "and (gpp.dateFrom is null or gpp.dateFrom <= :date) " +
                                                                            "and (gpp.dateTo is null or :date <= gpp.dateTo)")})
public class GrantProgramParam extends GryfEntity {

    //STATIC FIELDS

    public static final String EMAIL_REPLAY_TO = "EMAIL_RT";
    public static final String EMAIL_FROM = "EMAIL_FROM";
    public static final String OWN_CONTRIBUTION_PERCENT = "OWN_CONT_P";

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_GRANT_PROGRAM_IN_DATE = "GrantProgramParam.findByGrantProgramInDate";

    //FIELDS

    @Id
    protected Long id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @JsonBackReference("programParams")
    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID")
    private GrantProgram grantProgram;

    @JsonBackReference("params")
    @ManyToOne
    @JoinColumn(name = "PARAM_ID")
    private GrantProgramParamType paramType;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public GrantProgramParamType getParamType() {
        return paramType;
    }

    public void setParamType(GrantProgramParamType grantProgramParamTypes) {
        this.paramType = grantProgramParamTypes;
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
        return Objects.equals(id, ((GrantProgramParam) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    
}
