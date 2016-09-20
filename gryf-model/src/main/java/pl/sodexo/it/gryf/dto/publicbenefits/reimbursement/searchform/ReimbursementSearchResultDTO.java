package pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform;

import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
public class ReimbursementSearchResultDTO {

    //FIELDS

    private Long id;

    private String invoiceNumber;

    private String trainingInstitutionVatRegNum;

    private String trainingInstitutionName;

    private DictionaryDTO status;

    private Long reimbursementDeliveryId;

    private Date deliveryDate;

    private Date announcementDate;

    private Date reimbursementDate;

    //GETTERS & SETTERS

    //CONSTRUCTORS

    public ReimbursementSearchResultDTO() {
    }

    public ReimbursementSearchResultDTO(Reimbursement entity) {
        this.id = entity.getId();
        this.invoiceNumber = entity.getInvoiceNumber();
        this.status = DictionaryDTO.create(entity.getStatus());

        if(entity.getReimbursementDelivery() != null) {
            ReimbursementDelivery reimbursementDelivery = entity.getReimbursementDelivery();
            this.reimbursementDeliveryId = reimbursementDelivery.getId();
            this.deliveryDate = reimbursementDelivery.getDeliveryDate();

            TrainingInstitution trainingInstitution = reimbursementDelivery.getTrainingInstitution();
            if(trainingInstitution != null){
                this.trainingInstitutionVatRegNum = trainingInstitution.getVatRegNum();
                this.trainingInstitutionName= trainingInstitution.getName();
            }
        }
        this.announcementDate = entity.getAnnouncementDate();
        this.reimbursementDate = entity.getReimbursementDate();
    }

    //STATIC METHODS - CREATE

    public static ReimbursementSearchResultDTO create(Reimbursement entity) {
        return entity != null ? new ReimbursementSearchResultDTO(entity) : null;
    }

    public static List<ReimbursementSearchResultDTO> createList(List<Reimbursement> entities) {
        List<ReimbursementSearchResultDTO> list = new ArrayList<>();
        for (Reimbursement entity : entities) {
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTrainingInstitutionVatRegNum() {
        return trainingInstitutionVatRegNum;
    }

    public void setTrainingInstitutionVatRegNum(String trainingInstitutionVatRegNum) {
        this.trainingInstitutionVatRegNum = trainingInstitutionVatRegNum;
    }

    public String getTrainingInstitutionName() {
        return trainingInstitutionName;
    }

    public void setTrainingInstitutionName(String trainingInstitutionName) {
        this.trainingInstitutionName = trainingInstitutionName;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public Long getReimbursementDeliveryId() {
        return reimbursementDeliveryId;
    }

    public void setReimbursementDeliveryId(Long reimbursementDeliveryId) {
        this.reimbursementDeliveryId = reimbursementDeliveryId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public Date getReimbursementDate() {
        return reimbursementDate;
    }

    public void setReimbursementDate(Date reimbursementDate) {
        this.reimbursementDate = reimbursementDate;
    }
}
