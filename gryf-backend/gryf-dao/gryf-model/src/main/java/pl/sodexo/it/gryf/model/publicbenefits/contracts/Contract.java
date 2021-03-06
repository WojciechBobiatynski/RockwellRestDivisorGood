package pl.sodexo.it.gryf.model.publicbenefits.contracts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.accounts.AccountContractPairGenerable;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString(exclude = {"individual", "enterprise"})
@Entity
@Table(name = "CONTRACTS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = Contract.FIND_BY_PROGRAM_DATES, query = "SELECT c FROM Contract c where c.grantProgram.id = :grantProgramId " +
                " and c.individual.id = :individualId " +
                " and :startDate BETWEEN c.signDate AND c.expiryDate")})
public class Contract extends VersionableEntity implements AccountContractPairGenerable<String> {

    //STATIC FIELDS - ATRIBUTES
    public static final String ID_ATTR_NAME = "id";
    public static final String CONTRACT_TYPE_ATTR_NAME = "contractType";
    public static final String INDIVIDUAL_ATTR_NAME = "individual";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";
    public static final String GRANT_PROGRAM_ATTR_NAME = "grantProgram";
    public static final String SIGN_DATE_ATTR_NAME = "signDate";
    public static final String EXPIRY_DATE_ATTR_NAME = "expiryDate";
    public static final String FIND_BY_PROGRAM_DATES = "contract.findContractByProgramAndDate";
    public static final String FIND_BY_PROGRAM_DATES_PARAMS_PROGRAM_ID = "grantProgramId";
    public static final String FIND_BY_PROGRAM_DATES_PARAMS_START_DATE = "startDate" ;
    public static final String FIND_BY_PROGRAM_PARAMS_IND_ID = "individualId" ;
    public static final String OWN_CONTRIBUTION_PERCENTAGE_ATTR_NAME = "ownContributionPercentage" ;

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_TYPE_ID")
    @NotNull(message = "Rodzaj umowy nie mo??e by?? pusty")
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "INDIVIDUAL_ID")
    @NotNull(message = "Dane uczestnika nie mog?? by?? puste")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_ID")
    private Enterprise enterprise;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID")
    @NotNull(message = "Program dofinansowania nie mo??e by?? pusty")
    private GrantProgram grantProgram;

    @Column(name = "SIGN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Data podpisania umowy nie mo??e by?? pusta")
    private Date signDate;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Data wa??no??ci umowy nie mo??e by?? pusta")
    private Date expiryDate;

    @Column(name = "CODE")
    @Size(max = 8, message = "Kod u??ytkownika musi zawiera?? maksymalnie 8 znak??w")
    private String code;


    /**
     * Numer konta do przelew??w. Wype??niniany przez triger.
     */
    @Column(name = "ACCOUNT_PAYMENT")
    @Size(max = 26, message = "Konto do wp??aty na bony musi zawiera?? maksymalnie 26 znak??w")
    private String accountPayment;

    @Column(name = "ADDRESS_INVOICE")
    @NotEmpty(message = "Adres do faktury nie mo??e by?? pusty")
    @Size(max = 200, message = "Adres do faktury M??P musi zawiera?? maksymalnie 200 znak??w")
    private String addressInvoice;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_INVOICE")
    @NotNull(message = "Kod do faktury nie mo??e by?? pusty")
    private ZipCode zipCodeInvoice;

    @Column(name = "ADDRESS_CORR")
    @NotEmpty(message = "Adres korespondencyjny nie mo??e by?? pusty")
    @Size(max = 200, message = "Adres korespondencyjny M??P musi zawiera?? maksymalnie 200 znak??w")
    private String addressCorr;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_CORR")
    @NotNull(message = "Kod korespondencyjny nie mo??e by?? pusty")
    private ZipCode zipCodeCorr;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONTRACT_TRAINING_CATEGORIES", schema = "APP_PBE",
            joinColumns = {@JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TRAINING_CATEGORY_ID", referencedColumnName = "ID")})
    @NotEmpty(message = "Kategoria us??ugi nie mo??e by?? pusta")
    private List<TrainingCategory> categories;

    //GETTERS && SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountPayment() {
        return accountPayment;
    }

    public void setAccountPayment(String accountPayment) {
        this.accountPayment = accountPayment;
    }

    public String getAddressInvoice() {
        return addressInvoice;
    }

    public void setAddressInvoice(String addressInvoice) {
        this.addressInvoice = addressInvoice;
    }

    public ZipCode getZipCodeInvoice() {
        return zipCodeInvoice;
    }

    public void setZipCodeInvoice(ZipCode zipCodeInvoice) {
        this.zipCodeInvoice = zipCodeInvoice;
    }

    public String getAddressCorr() {
        return addressCorr;
    }

    public void setAddressCorr(String addressCorr) {
        this.addressCorr = addressCorr;
    }

    public ZipCode getZipCodeCorr() {
        return zipCodeCorr;
    }

    public void setZipCodeCorr(ZipCode zipCodeCorr) {
        this.zipCodeCorr = zipCodeCorr;
    }

    //LIST METHODS

    private List<TrainingCategory> getInitializedCategories() {
        if (categories == null)
            categories = new ArrayList<>();
        return categories;
    }

    public List<TrainingCategory> getCategories() {
        return Collections.unmodifiableList(getInitializedCategories());
    }

    public void addCategory(TrainingCategory category) {
        if (category.getId() == null || !getInitializedCategories().contains(category)) {
            getInitializedCategories().add(category);
        }
    }
}