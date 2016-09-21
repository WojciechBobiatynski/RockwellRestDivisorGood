package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.math.BigDecimal;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
public class OrderElementComplexTypeGrantedVouchersInfoDTO extends OrderElementDTO {

    //FIELDS

    /**
     * Przysługująca liczba bonów
     */
    private Integer entitledVouchersNumber;

    /**
     * Przysługująca wysokość pomocy w PLN
     */
    private BigDecimal entitledPlnAmount;

    /**
     * Wnioskowana liczba bonów
     */
    private Integer requestedVouchersNumber;

    /**
     * Limit pomocy de minimis w EUR
     */
    private BigDecimal limitEurAmount;

    /**
     * Kurs EUR
     */
    private BigDecimal exchange;

    /**
     * Przyznana liczba bonów
     */
    private Integer givenVouchersNumber;
    
    private BigDecimal voucherAidValue;

    //CONSTRUCTORS

    public OrderElementComplexTypeGrantedVouchersInfoDTO() {
    }

    public OrderElementComplexTypeGrantedVouchersInfoDTO(OrderElementDTOBuilder builder,
                                                         Integer entitledVouchersNumber,
                                                         BigDecimal entitledPlnAmount, BigDecimal limitEurAmount,
                                                         BigDecimal exchange) {
        super(builder);
        Order order = builder.getOrder();

        this.entitledVouchersNumber = entitledVouchersNumber;
        this.entitledPlnAmount = entitledPlnAmount;
        this.requestedVouchersNumber = order.getApplication().getBasicData().getVouchersNumber();
        this.limitEurAmount = limitEurAmount;
        this.exchange = exchange;
        if (order.getVouchersNumber() != null){
            this.givenVouchersNumber = order.getVouchersNumber();
        } else{
            this.givenVouchersNumber = Math.min(entitledVouchersNumber, order.getApplication().getBasicData().getVouchersNumber());
        }
        this.voucherAidValue = (order.getProduct() != null) ? order.getProduct().getPbeAidValue() : null;
    }

    //GETTERS & SETTERS


    public Integer getEntitledVouchersNumber() {
        return entitledVouchersNumber;
    }

    public void setEntitledVouchersNumber(Integer entitledVouchersNumber) {
        this.entitledVouchersNumber = entitledVouchersNumber;
    }

    public BigDecimal getEntitledPlnAmount() {
        return entitledPlnAmount;
    }

    public void setEntitledPlnAmount(BigDecimal entitledPlnAmount) {
        this.entitledPlnAmount = entitledPlnAmount;
    }

    public BigDecimal getLimitEurAmount() {
        return limitEurAmount;
    }

    public void setLimitEurAmount(BigDecimal limitEurAmount) {
        this.limitEurAmount = limitEurAmount;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public Integer getGivenVouchersNumber() {
        return givenVouchersNumber;
    }

    public void setGivenVouchersNumber(Integer givenVouchersNumber) {
        this.givenVouchersNumber = givenVouchersNumber;
    }

    public Integer getRequestedVouchersNumber() {
        return requestedVouchersNumber;
    }

    public void setRequestedVouchersNumber(Integer requestedVouchersNumber) {
        this.requestedVouchersNumber = requestedVouchersNumber;
    }

    public BigDecimal getVoucherAidValue() {
        return voucherAidValue;
    }

    public void setVoucherAidValue(BigDecimal voucherAidValue) {
        this.voucherAidValue = voucherAidValue;
    }
    
    
}
