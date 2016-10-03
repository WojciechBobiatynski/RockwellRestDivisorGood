package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.SearchDto;

import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
public class ReimbursementDeliverySearchQueryDTO extends SearchDto {

    //FIELDS

    private Long id;

    private String statusId;

    private Long trainingInstitutionId;

    private String trainingInstitutionName;

    private String trainingInstitutionVatRegNum;

    private String deliveryAddress;

    private String deliveryZipCode;

    private String deliveryCityName;

    private Date plannedReceiptDateFrom;

    private Date plannedReceiptDateTo;

    private Date requestDateFrom;

    private Date requestDateTo;

    private Date deliveryDateFrom;

    private Date deliveryDateTo;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
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

    public String getDeliveryCityName() {
        return deliveryCityName;
    }

    public void setDeliveryCityName(String deliveryCityName) {
        this.deliveryCityName = deliveryCityName;
    }

    public Date getPlannedReceiptDateFrom() {
        return plannedReceiptDateFrom;
    }

    public void setPlannedReceiptDateFrom(Date plannedReceiptDateFrom) {
        this.plannedReceiptDateFrom = plannedReceiptDateFrom;
    }

    public Date getPlannedReceiptDateTo() {
        return plannedReceiptDateTo;
    }

    public void setPlannedReceiptDateTo(Date plannedReceiptDateTo) {
        this.plannedReceiptDateTo = plannedReceiptDateTo;
    }

    public Date getRequestDateFrom() {
        return requestDateFrom;
    }

    public void setRequestDateFrom(Date requestDateFrom) {
        this.requestDateFrom = requestDateFrom;
    }

    public Date getRequestDateTo() {
        return requestDateTo;
    }

    public void setRequestDateTo(Date requestDateTo) {
        this.requestDateTo = requestDateTo;
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
}
