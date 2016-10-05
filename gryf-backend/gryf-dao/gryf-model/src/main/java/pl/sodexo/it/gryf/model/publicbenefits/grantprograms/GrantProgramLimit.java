package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = {"limitType", "enterpriseSize", "program"})
@Entity
@Table(name = "GRANT_PROGRAM_LIMITS", schema = "APP_PBE")
@NamedQueries(
        {@NamedQuery(name = GrantProgramLimit.FIND_BY_GRANT_PROGRAM_ENT_SIE_LIM_TYPE_IN_DATE, query="select gpl from GrantProgramLimit gpl " +
                                                                            "where gpl.program.id = :grantProgramId " +
                                                                            "and gpl.enterpriseSize.id = :enterpriseSizeId " +
                                                                            "and gpl.limitType = :limitType " +
                                                                            "and (gpl.dateFrom is null or gpl.dateFrom <= :date) " +
                                                                            "and (gpl.dateTo is null or :date <= gpl.dateTo)")})
public class GrantProgramLimit extends GryfEntity {
    public enum LimitType{
        /**
         * Standardowy limit bonów dla zamówień
         */
        ORDVOULIM("Standardowy limit bonów dla zamówień");
        //FIELDS
        private String label;
        //CONSTRUCTORS
        private LimitType(String label){
            this.label = label;
        }

        //GETTERS
        public String getLabel() {
            return label;
        }
    }
    public static final String FIND_BY_GRANT_PROGRAM_ENT_SIE_LIM_TYPE_IN_DATE = "GrantProgramLimit.findByGrantProgramEntSizeLimitTypeInDate";
    
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "LIMIT_TYPE")
    @Enumerated(EnumType.STRING)
    private LimitType limitType;

    @Column(name = "LIMIT_VALUE")
    private BigDecimal limitValue;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_SIZE_ID", referencedColumnName = "ID")
    private EnterpriseSize enterpriseSize;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    private GrantProgram program;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(BigDecimal limitValue) {
        this.limitValue = limitValue;
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

    public EnterpriseSize getEnterpriseSize() {
        return enterpriseSize;
    }

    public void setEnterpriseSize(EnterpriseSize enterpriseSize) {
        this.enterpriseSize = enterpriseSize;
    }

    public GrantProgram getProgram() {
        return program;
    }

    public void setProgram(GrantProgram program) {
        this.program = program;
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
        return Objects.equals(id, ((GrantProgramLimit) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
