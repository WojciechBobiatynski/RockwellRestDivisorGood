package pl.sodexo.it.gryf.model.publicbenefits.enterprises;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.VatRegNumFormat;
import pl.sodexo.it.gryf.model.accounts.AccountContractPairGenerable;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-22.
 */
@ToString(exclude = {"zipCodeInvoice", "zipCodeCorr", "contacts", "orders", "employments"})
@Entity
@Table(name = "ENTERPRISES", schema = "APP_PBE")
@SequenceGenerator(name="ind_seq", schema = "eagle", sequenceName = "ind_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Enterprise.findByVatRegNum", query = "select e from Enterprise e where e.vatRegNum = :vatRegNum order by e.addressCorr"),
        @NamedQuery(name = "Enterprise.getForUpdate", query = "select e from Enterprise e left join fetch e.contacts where e.id = :id"),
})
@OptimisticLocking(cascade=true)
public class Enterprise extends VersionableEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_VAT_REG_NUM = "Enterprise.findByVatRegNum";
    public static final String GET_FOR_UPDATE = "Enterprise.getForUpdate";

    //STATIC FIELDS - ATRIBUTES

    public static final String ID_ATTR_NAME = "id";
    public static final String ACCOUNT_REPAYMENT_NAME = "accountRepayment";
    public static final String NAME_ATTR_NAME = "name";
    public static final String VAT_REG_NUM_ATTR_NAME = "vatRegNum";
    public static final String ADDRESS_INVOICE_ATTR_NAME = "addressInvoice";
    public static final String ZIP_CODE_INVOICE_ATTR_NAME = "zipCodeInvoice";
    public static final String ADDRESS_CORR_ATTR_NAME = "addressCorr";
    public static final String ZIP_CODE_CORR_ATTR_NAME = "zipCodeCorr";
    public static final String REMARKS_ATTR_NAME = "remarks";
    public static final String CONTACTS_ATTR_NAME = "contacts";
    public static final String PBE_ORDER_LIST_ATTR_NAME = "orders";


    //FIELDS
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ind_seq")
    private Long id;

    @Column(name = "ACCOUNT_REPAYMENT")
    @Pattern(message = "Numer bankowy musi zawiera?? 26 cyfr", regexp = "^[0-9]{26}$")
    @Size(max = 26, message = "Numer bankowy M??P musi zawiera?? maksymalnie 26 znak??w")
    private String accountRepayment;

    @Column(name = "NAME")
    @NotEmpty(message = "Nazwa nie mo??e by?? pusta")
    @Size(max = 500, message = "Nazwa M??P musi zawiera?? maksymalnie 500 znak??w")
    private String name;

    @Column(name = "VAT_REG_NUM")
    @NotEmpty(message = "NIP nie mo??e by?? pusty")
    @VatRegNumFormat(message = "B??edny format NIP")
    @Size(max = 20, message = "NIP dla M??P musi zawiera?? maksymalnie 20 znak??w")
    private String vatRegNum;

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

    @Column(name = "REMARKS")
    @Size(max = 4000, message = "Uwagi dla u??ytkownika musz?? zawiera?? maksymalnie 4000 znak??w")
    private String remarks;

    @Valid
    @JsonManagedReference(Enterprise.CONTACTS_ATTR_NAME)
    @OneToMany(mappedBy = EnterpriseContact.ENTERPRISE_ATTR_NAME, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnterpriseContact> contacts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterprise")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterprise")
    @Getter
    @Setter
    private List<Employment> employments;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getAccountRepayment() {
        return accountRepayment;
    }

    public void setAccountRepayment(String accountRepayment) {
        this.accountRepayment = accountRepayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVatRegNum() {
        return vatRegNum;
    }

    public void setVatRegNum(String vatRegNum) {
        this.vatRegNum = vatRegNum;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //LIST METHODS

    private List<EnterpriseContact> getInitializedEnterpriseContacts() {
        if (contacts == null)
            contacts = new ArrayList<>();
        return contacts;
    }

    public List<EnterpriseContact> getContacts() {
        return Collections.unmodifiableList(getInitializedEnterpriseContacts());
    }

    public void addContact(EnterpriseContact contact) {
        if (contact.getEnterprise() != null && contact.getEnterprise() != this) {
            contact.getEnterprise().getInitializedEnterpriseContacts().remove(contact);
        }
        if (contact.getId() == null || !getInitializedEnterpriseContacts().contains(contact)) {
            getInitializedEnterpriseContacts().add(contact);
        }
        contact.setEnterprise(this);
    }

    private List<Order> getInitializedOrderList() {
        if (orders == null)
            orders = new ArrayList<>();
        return orders;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(getInitializedOrderList());
    }

    public void addOrder(Order order) {
        if (order.getEnterprise() != null && order.getEnterprise() != this) {
            order.getEnterprise().getInitializedOrderList().remove(order);
        }
        if (order.getId() == null || !getInitializedOrderList().contains(order)) {
            getInitializedOrderList().add(order);
        }
        order.setEnterprise(this);
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
        return Objects.equals(id, ((Enterprise) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
