package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform;

import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
public class ReimbursementDeliverySearchResultDTO {

    //FIELDS

    private Long id;

    private DictionaryDTO status;

    private Long trainingInstitutionId;

    private String trainingInstitutionName;

    private String trainingInstitutionVatRegNum;

    private String deliveryAddress;

    private String deliveryZipCode;

    private String deliveryCity;

    private Date plannedReceiptDate;

    private Date requestDate;

    private Date deliveryDate;

    private DictionaryDTO reimbursementPattern;

    private Date reimbursementAnnouncementDate;

    //CONSTRUCTORS

    public ReimbursementDeliverySearchResultDTO() {
    }

    public ReimbursementDeliverySearchResultDTO(ReimbursementDelivery entity) {
        this.id = entity.getId();
        this.status = DictionaryDTO.create(entity.getStatus());
        if(entity.getTrainingInstitution() != null) {
            this.trainingInstitutionId = entity.getTrainingInstitution().getId();
            this.trainingInstitutionName = entity.getTrainingInstitution().getName();
            this.trainingInstitutionVatRegNum = entity.getTrainingInstitution().getVatRegNum();
        }
        this.deliveryAddress = entity.getDeliveryAddress();
        this.deliveryZipCode = entity.getDeliveryZipCode();
        this.deliveryCity = entity.getDeliveryCityName();
        this.plannedReceiptDate = entity.getPlannedReceiptDate();
        this.requestDate = entity.getRequestDate();
        this.deliveryDate = entity.getDeliveryDate();
        this.reimbursementPattern = DictionaryDTO.create(entity.getReimbursementPattern());
        this.reimbursementAnnouncementDate = entity.getReimbursementAnnouncementDate();
    }

    //STATIC METHODS - CREATE

    public static ReimbursementDeliverySearchResultDTO create(ReimbursementDelivery entity) {
        return entity != null ? new ReimbursementDeliverySearchResultDTO(entity) : null;
    }

    public static List<ReimbursementDeliverySearchResultDTO> createList(List<ReimbursementDelivery> entities) {
        List<ReimbursementDeliverySearchResultDTO> list = new ArrayList<>();
        for (ReimbursementDelivery entity : entities) {
            list.add(create(entity));
        }
        return list;
    }

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public Long getTrainingInstitutionId() {
        return trainingInstitutionId;
    }

    public void setTrainingInstitutionId(Long trainingInstitutionId) {
        this.trainingInstitutionId = trainingInstitutionId;
    }

    public String getTrainingInstitutionName() {
        return trainingInstitutionName;
    }

    public void setTrainingInstitutionName(String trainingInstitutionName) {
        this.trainingInstitutionName = trainingInstitutionName;
    }

    public String getTrainingInstitutionVatRegNum() {
        return trainingInstitutionVatRegNum;
    }

    public void setTrainingInstitutionVatRegNum(String trainingInstitutionVatRegNum) {
        this.trainingInstitutionVatRegNum = trainingInstitutionVatRegNum;
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

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
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

    public DictionaryDTO getReimbursementPattern() {
        return reimbursementPattern;
    }

    public void setReimbursementPattern(DictionaryDTO reimbursementPattern) {
        this.reimbursementPattern = reimbursementPattern;
    }

    public Date getReimbursementAnnouncementDate() {
        return reimbursementAnnouncementDate;
    }

    public void setReimbursementAnnouncementDate(Date reimbursementAnnouncementDate) {
        this.reimbursementAnnouncementDate = reimbursementAnnouncementDate;
    }
}
