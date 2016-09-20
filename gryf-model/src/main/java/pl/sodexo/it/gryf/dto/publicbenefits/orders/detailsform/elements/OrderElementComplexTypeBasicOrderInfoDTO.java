package pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements;

import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
public class OrderElementComplexTypeBasicOrderInfoDTO extends OrderElementDTO {

    //FIELDS

    private Long orderId;

    private String statusId;
    
    private String statusName;

    private Date orderDate;

    private String operator;

    private Long enterpriseId;

    private String enterpriseName;

    private String vatRegNum;
    
    private Long individualId;
    
    private String individualName;
    
    private String pesel;

    //CONSTRUCTORS

    public OrderElementComplexTypeBasicOrderInfoDTO() {
    }

    public OrderElementComplexTypeBasicOrderInfoDTO(OrderElementDTOBuilder builder) {
        super(builder);
        Order order = builder.getOrder();
        this.orderId = order.getId();
        this.statusId = order.getStatus().getStatusId();
        this.statusName = order.getStatus().getStatusName();
        this.orderDate = order.getOrderDate();
        this.operator = order.getOperator();
        this.enterpriseId = (order.getEnterprise() != null) ? order.getEnterprise().getId() : null;
        this.enterpriseName = (order.getEnterprise() != null) ? order.getEnterprise().getName() : null;
        this.vatRegNum = (order.getEnterprise() != null) ? order.getEnterprise().getVatRegNum() : null;
        this.individualId = (order.getIndividual() != null) ? order.getIndividual().getId() : null;
        this.individualName = (order.getIndividual() != null) ? order.getIndividual().getFirstName() + " " + order.getIndividual().getLastName() : null;
        this.pesel = (order.getIndividual() != null) ? order.getIndividual().getPesel() : null;
    }

    //GETTERS & SETTERS

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getVatRegNum() {
        return vatRegNum;
    }

    public void setVatRegNum(String vatRegNum) {
        this.vatRegNum = vatRegNum;
    }

    public Long getIndividualId() {
        return individualId;
    }

    public void setIndividualId(Long individualId) {
        this.individualId = individualId;
    }

    public String getIndividualName() {
        return individualName;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    
}
