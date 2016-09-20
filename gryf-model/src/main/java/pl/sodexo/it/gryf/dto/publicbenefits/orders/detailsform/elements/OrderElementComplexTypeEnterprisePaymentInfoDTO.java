package pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements;

import java.math.BigDecimal;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;


/**
 *
 * @author Marcel.GOLUNSKI
 */
public class OrderElementComplexTypeEnterprisePaymentInfoDTO extends OrderElementDTO {

    //FIELDS

    private Integer givenVouchersNumber;

    private BigDecimal paymentAmount;
    
    private String paymentAccount;

    //CONSTRUCTORS

    public OrderElementComplexTypeEnterprisePaymentInfoDTO() {
    }

    public OrderElementComplexTypeEnterprisePaymentInfoDTO(OrderElementDTOBuilder builder, BigDecimal paymentAmount) {
        super(builder);
        Order order = builder.getOrder();
        this.givenVouchersNumber = order.getVouchersNumber();
        this.paymentAmount = paymentAmount;
        this.paymentAccount = order.getEnterprise() != null ? order.getEnterprise().getAccountPayment() : order.getIndividual().getAccountPayment();
    }

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
