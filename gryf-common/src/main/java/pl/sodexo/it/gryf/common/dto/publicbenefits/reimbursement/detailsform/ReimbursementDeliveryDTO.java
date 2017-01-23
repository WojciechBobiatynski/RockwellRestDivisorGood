package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.validation.InsertablePrivilege;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupDeliverReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupRegisterReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupSecondaryReimbursementDelivery;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
public class ReimbursementDeliveryDTO extends VersionableDto{

    //STATIC FIELDS - ATRIBUTES
    public static final String PARENT_ID_ATTR_NAME = "parentId";
    public static final String DELIVERY_DATE_ATTR_NAME = "deliveryDate";
    public static final String WAYBILL_NUMBER_ATTR_NAME = "waybillNumber";

    //FIELDS

    private Long id;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Wzorzec rozliczeń'", privileges = {Privileges.GRF_PBE_DELIVERIES_ACCEPT, Privileges.GRF_PBE_DELIVERIES_REG})
    @NotNull(message = "Wzorzec rozliczeń nie może być pusty", groups = {ValidationGroupRegisterReimbursementDelivery.class, ValidationGroupDeliverReimbursementDelivery.class})
    private DictionaryDTO reimbursementPattern;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Usługodawca'", privileges = {Privileges.GRF_PBE_DELIVERIES_ACCEPT, Privileges.GRF_PBE_DELIVERIES_REG})
    @NotNull(message = "Pole 'Usługodawca' nie może być puste", groups = {ValidationGroupRegisterReimbursementDelivery.class, ValidationGroupDeliverReimbursementDelivery.class})
    private TrainingInstitutionSearchResultDTO trainingInstitution;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Adres odbioru dostawy'", privileges = Privileges.GRF_PBE_DELIVERIES_REG)
    @NotNull(message = "Adres odbioru dostawy nie może być pusty", groups = ValidationGroupRegisterReimbursementDelivery.class)
    @Size(message = "Pole 'Adres odbioru dostawy' może mieć długość maksymalnie 200 znaków", max = 200)
    private String deliveryAddress;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Kod odbioru dostawy'", privileges = Privileges.GRF_PBE_DELIVERIES_REG)
    @NotNull(message = "Kod odbioru dostawy nie może być pusty", groups = ValidationGroupRegisterReimbursementDelivery.class)
    @Size(message = "Pole 'Kod odbioru dostawy' może mieć długość maksymalnie 10 znaków", max = 10)
    private String deliveryZipCode;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Miasto odbioru dostawy'", privileges = Privileges.GRF_PBE_DELIVERIES_REG)
    @NotNull(message = "Miasto odbioru dostawy nie może być puste", groups = ValidationGroupRegisterReimbursementDelivery.class)
    @Size(message = "Pole 'Miasto odbioru dostawy' może mieć długość maksymalnie 100 znaków", max = 100)
    private String deliveryCityName;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Planowana data odbioru kuponów'", privileges = Privileges.GRF_PBE_DELIVERIES_REG)
    @NotNull(message = "Planowana data odbioru kuponów nie może być pusta", groups = ValidationGroupRegisterReimbursementDelivery.class)
    private Date plannedReceiptDate;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Data przyjęcia zgłoszenia'", privileges = Privileges.GRF_PBE_DELIVERIES_REG)
    @NotNull(message = "Data przyjęcia zgłoszenia nie może być pusta", groups = ValidationGroupRegisterReimbursementDelivery.class)
    private Date requestDate;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Data otrzymania przesyłki'", privileges = Privileges.GRF_PBE_DELIVERIES_ACCEPT)
    @NotNull(message = "Data otrzymania przesyłki nie może być pusta", groups = ValidationGroupDeliverReimbursementDelivery.class)
    private Date deliveryDate;

    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Numer listu przewozowego'", privileges = Privileges.GRF_PBE_DELIVERIES_ACCEPT)
    @NotEmpty(message = "Numer listu przewozowego nie może być pusty", groups = ValidationGroupDeliverReimbursementDelivery.class)
    @Size(message = "Pole 'Numer listu przewozowego' może mieć długość maksymalnie 50 znaków", max = 50)
    private String waybillNumber;

    private DictionaryDTO status;

    private boolean sodexoRealization;

    @NotNull(message = "Id dostawy podrzędnej nie może być puste", groups = ValidationGroupSecondaryReimbursementDelivery.class)
    @InsertablePrivilege(message = "Nie posiadasz uprawnień do edycji pola 'Id dostawy podrzędnej'", privileges = Privileges.GRF_PBE_DELIVERIES_REIMB)
    private Long parentId;

    private Date announcementDate;

    private boolean deliverySecondary;

    @Size(message = "Pole 'Opis' może mieć długość maksymalnie 2000 znaków", max = 2000)
    private String remarks;

    private Integer version;

    private List<String> acceptedViolations;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictionaryDTO getReimbursementPattern() {
        return reimbursementPattern;
    }

    public void setReimbursementPattern(DictionaryDTO reimbursementPattern) {
        this.reimbursementPattern = reimbursementPattern;
    }

    public TrainingInstitutionSearchResultDTO getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(TrainingInstitutionSearchResultDTO trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryZipCode() {
        return deliveryZipCode;
    }

    public void setDeliveryZipCode(String deliveryZipCode) {
        this.deliveryZipCode = deliveryZipCode;
    }

    public String getDeliveryCityName() {
        return deliveryCityName;
    }

    public void setDeliveryCityName(String deliveryCityName) {
        this.deliveryCityName = deliveryCityName;
    }

    public Date getPlannedReceiptDate() {
        return plannedReceiptDate;
    }

    public void setPlannedReceiptDate(Date plannedReceiptDate) {
        this.plannedReceiptDate = plannedReceiptDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public boolean isSodexoRealization() {
        return sodexoRealization;
    }

    public void setSodexoRealization(boolean sodexoRealization) {
        this.sodexoRealization = sodexoRealization;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isDeliverySecondary() {
        return deliverySecondary;
    }

    public void setDeliverySecondary(boolean deliverySecondary) {
        this.deliverySecondary = deliverySecondary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<String> getAcceptedViolations() {
        return acceptedViolations;
    }

    public void setAcceptedViolations(List<String> acceptedViolations) {
        this.acceptedViolations = acceptedViolations;
    }

}
