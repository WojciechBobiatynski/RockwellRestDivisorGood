package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO which is send to front-end as a result of searching operation.
 *
 * Created by Michal.CHWEDCZUK.ext on 2015-07-28.
 */
@ToString
public class OrderSearchResultDTO {

    //PRIVATE FIELDS

    private Long id;

    private DictionaryDTO status;

    private Date orderDate;

    private Long applicationId;

    private Long enterpriseId;

    private String enterpriseName;

    private String vatRegNum;
    
    private Long individualId;
    
    private String individualName;
    
    private String pesel;

    private String operator;

    private Date minRequiredDate;

    //CONSTRUCTORS & CREATED LIST

    private OrderSearchResultDTO(){
    }

    private OrderSearchResultDTO(Order order, Date minRequiredDate){
        this.id = order.getId();
        this.status = DictionaryDTO.create(order.getStatus());
        this.orderDate = order.getOrderDate();
        this.applicationId = (order.getApplication() != null) ? order.getApplication().getId() : null;
        this.enterpriseId = (order.getEnterprise() != null) ? order.getEnterprise().getId() : null;
        this.enterpriseName = (order.getEnterprise() != null) ? order.getEnterprise().getName() : null;
        this.vatRegNum = (order.getEnterprise() != null) ? order.getEnterprise().getVatRegNum() : null;
        this.individualId = (order.getIndividual() != null) ? order.getIndividual().getId() : null;
        this.individualName = (order.getIndividual() != null) ? order.getIndividual().getFirstName() + " " + order.getIndividual().getLastName() : null;
        this.pesel = (order.getIndividual() != null) ? order.getIndividual().getPesel() : null;
        this.operator = order.getOperator();
        this.minRequiredDate = minRequiredDate;
    }

    //STATIC METHODS - CREATE

    public static OrderSearchResultDTO create(Order entity, Date minRequiredDate) {
        return entity != null ? new OrderSearchResultDTO(entity, minRequiredDate) : null;
    }

    public static List<OrderSearchResultDTO> createList(List<Object[]> entities) {
        List<OrderSearchResultDTO> list = new ArrayList<>();
        for (Object[] entity : entities) {
            list.add(create((Order)entity[0], (Date)entity[1]));
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getMinRequiredDate() {
        return minRequiredDate;
    }

    public void setMinRequiredDate(Date minRequiredDate) {
        this.minRequiredDate = minRequiredDate;
    }
}
