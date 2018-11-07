package pl.sodexo.it.gryf.model.publicbenefits.individuals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.PeselFormat;
import pl.sodexo.it.gryf.model.accounts.AccountContractPairGenerable;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual.EXISTS_PESEL;

/**
 * Created by michal.szymczyk on 2016-02-26.
 */
@ToString(exclude = {"zipCodeInvoice", "zipCodeCorr", "enterprise", "contacts", "orders", "employments"})
@Entity
@Table(name = "INDIVIDUALS", schema = "APP_PBE")
@SequenceGenerator(name="ind_seq", schema = "eagle", sequenceName = "ind_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Individual.findByPesel", query = "select i from Individual i where i.pesel = :pesel order by i.addressCorr"),
        @NamedQuery(name = EXISTS_PESEL, query = "select count(i) from Individual i where i.pesel = :pesel order by i.addressCorr"),
        @NamedQuery(name = "Individual.getForUpdate", query = "select i from Individual i left join fetch i.contacts left join fetch i.individualUser.roles where i.id = :id"),
        @NamedQuery(name = "Individual.findById", query = "select i from Individual i where i.id= :id"),
})
@OptimisticLocking(cascade=true)
public class Individual extends VersionableEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_PESEL = "Individual.findByPesel";
    public static final String EXISTS_PESEL = "Individual.existPesel";
    public static final String GET_FOR_UPDATE = "Individual.getForUpdate";
    public static final String FIND_BY_ID= "Individual.findById";

    //STATIC FIELDS - ATRIBUTES

    public static final String ID_ATTR_NAME = "id";
    public static final String CODE_ATTR_NAME = "code";
    public static final String ACCOUNT_PAYMENT_ATTR_NAME = "accountPayment";
    public static final String ACCOUNT_REPAYMENT_NAME = "accountRepayment";
    public static final String FIRST_NAME_ATTR_NAME = "firstName";
    public static final String LAST_NAME_ATTR_NAME = "lastName";
    public static final String PESEL_ATTR_NAME = "pesel";
    public static final String SEX_ATTR_NAME = "sex";
    public static final String DOCUMENT_TYPE_ATTR_NAME = "documentType";
    public static final String DOCUMENT_NUMBER_ATTR_NAME = "documentNumber";
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
    @Pattern(message = "Numer bankowy musi zawierać 26 cyfr", regexp = "^[0-9]{26}$")
    @Size(max = 26, message = "Numer bankowy użytkownika musi zawierać maksymalnie 26 znaków")
    private String accountRepayment;

    @Column(name = "FIRST_NAME")
    @NotEmpty(message = "Imię nie może być puste")
    @Size(max = 100, message = "Imię użytkownika musi zawierać maksymalnie 100 znaków")
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotEmpty(message = "Nazwisko nie może być puste")
    @Size(max = 300, message = "Nazwisko użytkownika musi zawierać maksymalnie 300 znaków")
    private String lastName;

    @Column(name = "PESEL")
    @NotEmpty(message = "PESEL nie może być pusty")
    @PeselFormat(message = "Błędny format PESEL")
    @Size(max = 11, message = "PESEL użytkownika musi zawierać maksymalnie 300 znaków")
    private String pesel;

    @Column(name = "SEX")
    @NotEmpty(message = "Płeć nie może być pusta")
    @Size(max = 1, message = "Płęć użytkownika musi zawierać maksymalnie 1 znak")
    private String sex;

    @Column(name = "DOCUMENT_TYPE")
    @Size(max = 30, message = "Rodzaj dokumentu użytkownika musi zawierać maksymalnie 30 znaków")
    private String documentType;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "ADDRESS_INVOICE")
    @NotEmpty(message = "Adres do faktury nie może być pusty")
    @Size(max = 200, message = "Adres do faktury użytkownika musi zawierać maksymalnie 200 znaków")
    private String addressInvoice;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_INVOICE")
    @NotNull(message = "Kod do faktury nie może być pusty")
    private ZipCode zipCodeInvoice;

    @Column(name = "ADDRESS_CORR")
    @NotEmpty(message = "Adres korespondencyjny nie może być pusty")
    @Size(max = 200, message = "Adres korespondencyjny użytkownika musi zawierać maksymalnie 200 znaków")
    private String addressCorr;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_CORR")
    @NotNull(message = "Kod korespondencyjny nie może być pusty")
    private ZipCode zipCodeCorr;

    @Column(name = "REMARKS")
    @Size(max = 4000, message = "Uwagi dla użytkownika muszą zawierać maksymalnie 4000 znaków")
    private String remarks;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "individual")
    private IndividualUser individualUser;

    @Valid
    @JsonManagedReference(Individual.CONTACTS_ATTR_NAME)
    @OneToMany(mappedBy = IndividualContact.INDIVIDUAL_ATTR_NAME, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndividualContact> contacts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "individual", orphanRemoval = true)
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public IndividualUser getIndividualUser() {
        return individualUser;
    }

    public void setIndividualUser(IndividualUser individualUser) {
        this.individualUser = individualUser;
    }

    //LIST METHODS

    private List<IndividualContact> getInitializedIndividualContacts() {
        if (contacts == null)
            contacts = new ArrayList<>();
        return contacts;
    }

    public List<IndividualContact> getContacts() {
        return Collections.unmodifiableList(getInitializedIndividualContacts());
    }

    public void addContact(IndividualContact contact) {
        if (contact.getIndividual() != null && contact.getIndividual() != this) {
            contact.getIndividual().getInitializedIndividualContacts().remove(contact);
        }
        if (contact.getId() == null || !getInitializedIndividualContacts().contains(contact)) {
            getInitializedIndividualContacts().add(contact);
        }
        contact.setIndividual(this);
    }

    private List<Employment> getInitializedEmploymentsList() {
        if (employments == null)
            employments = new ArrayList<>();
        return employments;
    }

    public List<Employment> getEmployments() {
        return Collections.unmodifiableList(getInitializedEmploymentsList());
    }

    public void addEmployment(Employment employment) {
        if (employment.getIndividual() != null && employment.getIndividual() != this) {
            employment.getIndividual().getInitializedEmploymentsList().remove(employment);
        }
        if (employment.getEmpId() == null || !getInitializedEmploymentsList().contains(employment)) {
            getInitializedEmploymentsList().add(employment);
        }
        employment.setIndividual(this);
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
        return Objects.equals(id, ((Individual) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
