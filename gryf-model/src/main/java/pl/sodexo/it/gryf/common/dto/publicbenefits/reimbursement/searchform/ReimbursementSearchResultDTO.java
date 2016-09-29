package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
public class ReimbursementSearchResultDTO extends GryfDto {

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
