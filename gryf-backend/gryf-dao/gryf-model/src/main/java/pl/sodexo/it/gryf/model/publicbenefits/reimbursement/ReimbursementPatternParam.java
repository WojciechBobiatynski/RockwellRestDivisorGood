package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString(exclude = "reimbursementPattern")
@Entity
@Table(name = "REIMBURSEMENT_PATTERN_PARAMS", schema = "APP_PBE")
@NamedQueries(
        {@NamedQuery(name = ReimbursementPatternParam.FIND_BY_REIMBURSEMENT_PATTERN_PARAM_IN_DATE, query="select distinct rpp from ReimbursementPatternParam rpp " +
                "where rpp.reimbursementPattern.id = :reimbursementPatternId " +
                "and rpp.paramId = :paramId " +
                "and (rpp.dateFrom is null or rpp.dateFrom <= :date) " +
                "and (rpp.dateTo is null or :date <= rpp.dateTo)")})
public class ReimbursementPatternParam extends GryfEntity{

    //STATIC FIELDS
    public static final String DELMMLIMIT = "DELMMLIMIT";
    public static final String REIMBDELAY = "REIMBDELAY";
    public static final String CORRDELAY = "CORRDELAY";


    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_REIMBURSEMENT_PATTERN_PARAM_IN_DATE = "ReimbursementPatternParam.findByReimbursementPatternParamInDate";

    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REIMBURSEMENT_PATTERN_ID", referencedColumnName = "ID")
    private ReimbursementPattern reimbursementPattern;

    @Column(name = "REIMB_PATTERN_PARAM_ID")
    private String paramId;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementPattern getReimbursementPattern() {
        return reimbursementPattern;
    }

    public void setReimbursementPattern(ReimbursementPattern reimbursementPattern) {
        this.reimbursementPattern = reimbursementPattern;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
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
        return Objects.equals(id, ((ReimbursementPatternParam) o).id);
    }
}
