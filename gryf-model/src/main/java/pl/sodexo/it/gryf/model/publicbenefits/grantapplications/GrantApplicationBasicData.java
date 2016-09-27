package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseEntityType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString
@Entity
@Table(name = "GRANT_APPLICATION_BASIC_DATA", schema = "APP_PBE")
public class GrantApplicationBasicData extends AuditableEntity {

    public static final String VAT_REG_NUM_ATTR_NAME = "vatRegNum";
    public static final String ADDRESS_INVOICE_ATTR_NAME = "addressInvoice";
    public static final String ZIP_CODE_INVOICE_ATTR_NAME = "zipCodeInvoice";
    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @JsonBackReference("basicData")
    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private GrantApplication application;

    @Column(name = "ENTERPRISE_NAME")
    private String enterpriseName;

    @Column(name = "VAT_REG_NUM")
    private String vatRegNum;

    @ManyToOne
    @JoinColumn(name = "ENTITY_TYPE_ID", referencedColumnName = "ID")
    private EnterpriseEntityType entityType;

    @Column(name = "ADDRESS_INVOICE")
    private String addressInvoice;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_INVOICE_ID", referencedColumnName = "ID")
    private ZipCode zipCodeInvoice;

    @Column(name = "COUNTY")
    private String county;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_SIZE_ID", referencedColumnName = "ID")
    private EnterpriseSize enterpriseSize;

    @Column(name = "VOUCHERS_NUMBER")
    private Integer vouchersNumber;

    @Column(name = "ADDRESS_CORR")
    private String addressCorr;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_CORR_ID", referencedColumnName = "ID")
    private ZipCode zipCodeCorr;

    @Column(name = "ACCOUNT_REPAYMENT")
    private String accountRepayment;

    @JsonManagedReference("pkdList")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicationBasic", orphanRemoval = true)
    private List<GrantApplicationPkd> pkdList;

    @JsonManagedReference("contacts")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicationBasic", orphanRemoval = true)
    private List<GrantApplicationContactData> contacts;

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

    public void setApplication(GrantApplication application) {
        this.application = application;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getVatRegNum() {
        return vatRegNum;
    }

    public void setVatRegNum(String vatRegNum) {
        this.vatRegNum = vatRegNum;
    }

    public EnterpriseEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EnterpriseEntityType entityType) {
        this.entityType = entityType;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public EnterpriseSize getEnterpriseSize() {
        return enterpriseSize;
    }

    public void setEnterpriseSize(EnterpriseSize enterpriseSize) {
        this.enterpriseSize = enterpriseSize;
    }

    public Integer getVouchersNumber() {
        return vouchersNumber;
    }

    public void setVouchersNumber(Integer vouchersNumber) {
        this.vouchersNumber = vouchersNumber;
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

    public String getAccountRepayment() {
        return accountRepayment;
    }

    public void setAccountRepayment(String accountRepayment) {
        this.accountRepayment = accountRepayment;
    }

    //LIST METHODS

    private List<GrantApplicationPkd> getInitializedPkdList() {
        if (pkdList == null)
            pkdList = new ArrayList<>();
        return pkdList;
    }

    public List<GrantApplicationPkd> getPkdList() {
        return Collections.unmodifiableList(getInitializedPkdList());
    }

    public void addPkd(GrantApplicationPkd pkd) {
        if (pkd.getApplicationBasic() != null && pkd.getApplicationBasic() != this) {
            pkd.getApplicationBasic().getInitializedPkdList().remove(pkd);
        }
        if (pkd.getId() == null || !getInitializedPkdList().contains(pkd)) {
            getInitializedPkdList().add(pkd);
        }
        pkd.setApplicationBasic(this);
    }

    public void removePkd(GrantApplicationPkd contact) {
        getInitializedPkdList().remove(contact);
    }

    private List<GrantApplicationContactData> getInitializedContacts() {
        if (contacts == null)
            contacts = new ArrayList<>();
        return contacts;
    }

    public List<GrantApplicationContactData> getContacts() {
        return Collections.unmodifiableList(getInitializedContacts());
    }

    public void addContact(GrantApplicationContactData contact) {
        if (contact.getApplicationBasic() != null && contact.getApplicationBasic() != this) {
            contact.getApplicationBasic().getInitializedContacts().remove(contact);
        }
        if (contact.getId() == null || !getInitializedContacts().contains(contact)) {
            getInitializedContacts().add(contact);
        }
        contact.setApplicationBasic(this);
    }

    public void removeContact(GrantApplicationContactData contact) {
        getInitializedContacts().remove(contact);
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
        return Objects.equals(id, ((GrantApplicationBasicData) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
