package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.VatRegNumFormat;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-22.
 */
@ToString(exclude = {"trainingInstitutionUsers","contacts"})
@Entity
@Table(name = "TRAINING_INSTITUTIONS", schema = "APP_PBE")
@SequenceGenerator(name="trin_seq", schema = "eagle", sequenceName = "trin_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "TrainingInstitution.findByVatRegNum", query = "select e from TrainingInstitution e where e.vatRegNum = :vatRegNum order by e.addressCorr"),
        @NamedQuery(name = "TrainingInstitution.getForUpdate", query = "select e from TrainingInstitution e left join fetch e.contacts left join fetch e.trainingInstitutionUsers u left join fetch u.roles where e.id = :id"),
        @NamedQuery(name = "TrainingInstitution.findByExternalId", query = "select e from TrainingInstitution e where e.externalId = :externalId "),
})
@OptimisticLocking(cascade=true)
public class TrainingInstitution extends VersionableEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_VAT_REG_NUM = "TrainingInstitution.findByVatRegNum";
    public static final String GET_FOR_UPDATE = "TrainingInstitution.getForUpdate";

    //STATIC FIELDS - ATRIBUTES

    public static final String ID_ATTR_NAME = "id";
    public static final String CODE_ATTR_NAME = "code";
    public static final String NAME_ATTR_NAME = "name";
    public static final String VAT_REG_NUM_ATTR_NAME = "vatRegNum";
    public static final String ADDRESS_INVOICE_ATTR_NAME = "addressInvoice";
    public static final String ZIP_CODE_INVOICE_ATTR_NAME = "zipCodeInvoice";
    public static final String ADDRESS_CORR_ATTR_NAME = "addressCorr";
    public static final String ZIP_CODE_CORR_ATTR_NAME = "zipCodeCorr";
    public static final String REMARKS_ATTR_NAME = "remarks";
    public static final String CONTACTS_ATTR_NAME = "contacts";

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "trin_seq")
    private Long id;

    @Column(name = "EXTERNAL_ID")
    @Size(max = 30, message = "Identyfikator zewn??trzny Us??ugodawcy musi zawiera?? maksymalnie 30 znak??w")
    private String externalId;

    @Column(name = "CODE")
    @Size(max = 8, message = "Kod Us??ugodawcy musi zawiera?? maksymalnie 8 znak??w")
    private String code;

    @Column(name = "NAME")
    @NotEmpty(message = "Nazwa Us??ugodawcy nie mo??e by?? pusta")
    @Size(max = 500, message = "Nazwa Us??ugodawcy musi zawiera?? maksymalnie 500 znak??w")
    private String name;

    @Column(name = "VAT_REG_NUM")
    @NotEmpty(message = "NIP nie mo??e by?? pusty")
    @VatRegNumFormat(message = "B??edny format NIP")
    @Size(max = 20, message = "NIP Us??ugodawcy musi zawiera?? maksymalnie 20 znak??w")
    private String vatRegNum;

    @Column(name = "ADDRESS_INVOICE")
    @NotEmpty(message = "Adres do faktury nie mo??e by?? pusty")
    @Size(max = 200, message = "Adres do faktury Us??ugodawcy musi zawiera?? maksymalnie 200 znak??w")
    private String addressInvoice;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_INVOICE")
    @NotNull(message = "Kod do faktury nie mo??e by?? pusty")
    private ZipCode zipCodeInvoice;

    @Column(name = "ADDRESS_CORR")
    @NotEmpty(message = "Adres korespondencyjny nie mo??e by?? pusty")
    @Size(max = 200, message = "Adres korespondencyjny Us??ugodawcy musi zawiera?? maksymalnie 200 znak??w")
    private String addressCorr;

    @ManyToOne
    @JoinColumn(name = "ZIP_CODE_CORR")
    @NotNull(message = "Kod korespondencyjny nie mo??e by?? pusty")
    private ZipCode zipCodeCorr;

    @Column(name = "REMARKS")
    @Size(max = 4000, message = "Uwagi dla Us??ugodawcy mus?? zawiera?? maksymalnie 4000 znak??w")
    private String remarks;

    @Valid
    @JsonManagedReference(TrainingInstitution.CONTACTS_ATTR_NAME)
    @OneToMany(mappedBy = TrainingInstitutionContact.TRAINING_INSTITUTION_ATTR_NAME, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingInstitutionContact> contacts;

    @Getter
    @Setter
    @OneToMany(mappedBy = "trainingInstitution", cascade = CascadeType.ALL)
    private List<TrainingInstitutionUser> trainingInstitutionUsers = new ArrayList<>();

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    private List<TrainingInstitutionContact> getInitializedEnterpriseContacts() {
        if (contacts == null)
            contacts = new ArrayList<>();
        return contacts;
    }

    public List<TrainingInstitutionContact> getContacts() {
        return Collections.unmodifiableList(getInitializedEnterpriseContacts());
    }

    public void addContact(TrainingInstitutionContact contact) {
        if (contact.getTrainingInstitution() != null && contact.getTrainingInstitution() != this) {
            contact.getTrainingInstitution().getInitializedEnterpriseContacts().remove(contact);
        }
        if (contact.getId() == null || !getInitializedEnterpriseContacts().contains(contact)) {
            getInitializedEnterpriseContacts().add(contact);
        }
        contact.setTrainingInstitution(this);
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
        return Objects.equals(id, ((TrainingInstitution) o).id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
