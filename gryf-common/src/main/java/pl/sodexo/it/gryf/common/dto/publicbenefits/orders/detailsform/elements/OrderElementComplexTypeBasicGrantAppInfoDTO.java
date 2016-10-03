package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@ToString
public class OrderElementComplexTypeBasicGrantAppInfoDTO extends OrderElementDTO {

    //FIELDS

    private Long applicationId;

    private Date receiptDate;

    private Date applyDate;

    private Date considerationDate;

    private String operator;

    //GETTERS & SETTERS

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getConsiderationDate() {
        return considerationDate;
    }

    public void setConsiderationDate(Date considerationDate) {
        this.considerationDate = considerationDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
