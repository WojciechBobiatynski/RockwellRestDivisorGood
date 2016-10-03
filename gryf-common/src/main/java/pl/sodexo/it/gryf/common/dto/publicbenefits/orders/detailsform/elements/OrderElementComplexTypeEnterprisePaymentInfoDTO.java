package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

import java.math.BigDecimal;


/**
 *
 * @author Marcel.GOLUNSKI
 */
@ToString
public class OrderElementComplexTypeEnterprisePaymentInfoDTO extends OrderElementDTO {

    //FIELDS

    private Integer givenVouchersNumber;

    private BigDecimal paymentAmount;
    
    private String paymentAccount;

    //GETTERS & SETTERS

    public Integer getGivenVouchersNumber() {
        return givenVouchersNumber;
    }

    public void setGivenVouchersNumber(Integer givenVouchersNumber) {
        this.givenVouchersNumber = givenVouchersNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

  
}
