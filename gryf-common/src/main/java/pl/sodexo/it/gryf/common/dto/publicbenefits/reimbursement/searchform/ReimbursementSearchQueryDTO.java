package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform;

import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
public class ReimbursementSearchQueryDTO extends SearchDto {

    //FIELDS

    private Long id;

    private String invoiceNumber;

    private String trainingInstitutionVatRegNum;

    private String trainingInstitutionName;

    private String statusId;

    private Long reimbursementDeliveryId;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date deliveryDateFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date deliveryDateTo;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date announcementDateFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date announcementDateTo;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date reimbursementDateFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date reimbursementDateTo;

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

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Long getReimbursementDeliveryId() {
        return reimbursementDeliveryId;
    }

    public void setReimbursementDeliveryId(Long reimbursementDeliveryId) {
        this.reimbursementDeliveryId = reimbursementDeliveryId;
    }

    public Date getDeliveryDateFrom() {
        return deliveryDateFrom;
    }

    public void setDeliveryDateFrom(Date deliveryDateFrom) {
        this.deliveryDateFrom = deliveryDateFrom;
    }

    public Date getDeliveryDateTo() {
        return deliveryDateTo;
    }

    public void setDeliveryDateTo(Date deliveryDateTo) {
        this.deliveryDateTo = deliveryDateTo;
    }

    public Date getAnnouncementDateFrom() {
        return announcementDateFrom;
    }

    public void setAnnouncementDateFrom(Date announcementDateFrom) {
        this.announcementDateFrom = announcementDateFrom;
    }

    public Date getAnnouncementDateTo() {
        return announcementDateTo;
    }

    public void setAnnouncementDateTo(Date announcementDateTo) {
        this.announcementDateTo = announcementDateTo;
    }

    public Date getReimbursementDateFrom() {
        return reimbursementDateFrom;
    }

    public void setReimbursementDateFrom(Date reimbursementDateFrom) {
        this.reimbursementDateFrom = reimbursementDateFrom;
    }

    public Date getReimbursementDateTo() {
        return reimbursementDateTo;
    }

    public void setReimbursementDateTo(Date reimbursementDateTo) {
        this.reimbursementDateTo = reimbursementDateTo;
    }
}