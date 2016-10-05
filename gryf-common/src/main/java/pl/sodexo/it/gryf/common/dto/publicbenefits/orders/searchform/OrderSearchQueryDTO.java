package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-08-26.
 */
@ToString
public class OrderSearchQueryDTO extends SearchDto {

    //STATIC FIELDS - ATRIBUTES

    public static final String MIN_REQUIRED_DATE_ATTR_NAME = "minRequiredDate";

    //PRIVATE FIELDS

    private Long id;

    private String statusId;

    private Date orderDateFrom;

    private Date orderDateTo;

    private Long applicationId;

    private Long enterpriseId;

    private String enterpriseName;

    private String vatRegNum;
    
    private Long individualId;
    
    private String individualFirstName;
    
    private String individualLastName;
    
    private String pesel;

    private String operator;

    private Date minRequiredDateFrom;

    private Date minRequiredDateTo;

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

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(Date orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
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

    public String getIndividualFirstName() {
        return individualFirstName;
    }

    public void setIndividualFirstName(String individualFirstName) {
        this.individualFirstName = individualFirstName;
    }

    public String getIndividualLastName() {
        return individualLastName;
    }

    public void setIndividualLastName(String individualLastName) {
        this.individualLastName = individualLastName;
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

    public Date getMinRequiredDateFrom() {
        return minRequiredDateFrom;
    }

    public void setMinRequiredDateFrom(Date minRequiredDateFrom) {
        this.minRequiredDateFrom = minRequiredDateFrom;
    }

    public Date getMinRequiredDateTo() {
        return minRequiredDateTo;
    }

    public void setMinRequiredDateTo(Date minRequiredDateTo) {
        this.minRequiredDateTo = minRequiredDateTo;
    }


    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }
}
