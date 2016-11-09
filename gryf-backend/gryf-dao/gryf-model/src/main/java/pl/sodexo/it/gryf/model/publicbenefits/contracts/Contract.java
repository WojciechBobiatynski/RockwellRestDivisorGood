package pl.sodexo.it.gryf.model.publicbenefits.contracts;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString(exclude = {"individual", "enterprise"})
@Entity
@Table(name = "CONTRACTS", schema = "APP_PBE")
@SequenceGenerator(name="cnt_seq", schema = "eagle", sequenceName = "cnt_seq", allocationSize = 1)
public class Contract extends VersionableEntity {

    //STATIC FIELDS - ATRIBUTES
    public static final String ID_ATTR_NAME = "id";
    public static final String CONTRACT_TYPE_ATTR_NAME = "contractType";
    public static final String INDIVIDUAL_ATTR_NAME = "individual";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";
    public static final String GRANT_PROGRAM_ATTR_NAME = "grantProgram";
    public static final String SIGN_DATE_ATTR_NAME = "signDate";
    public static final String EXPIRY_DATE_ATTR_NAME = "expiryDate";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "cnt_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_TYPE_ID")
    @NotNull(message = "Rodzaj umowy nie może byc pusty")
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "INDIVIDUAL_ID")
    @NotNull(message = "Dane uczestnika nie mogą być puste")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_ID")
    private Enterprise enterprise;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID")
    @NotNull(message = "Program dofinansowania nie może być pusty")
    private GrantProgram grantProgram;

    @Column(name = "SIGN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Data podpisania umowy nie może być pusta")
    private Date signDate;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    //GETTERS && SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}