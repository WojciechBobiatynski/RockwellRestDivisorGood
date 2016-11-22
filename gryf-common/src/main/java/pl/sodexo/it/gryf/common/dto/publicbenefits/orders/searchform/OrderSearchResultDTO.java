package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;

import java.util.Date;

/**
 * DTO which is send to front-end as a result of searching operation.
 * <p/>
 * Created by Michal.CHWEDCZUK.ext on 2015-07-28.
 */
@ToString
public class OrderSearchResultDTO extends GryfDto {

    //PRIVATE FIELDS

    private Long id;

    private String grantProgramName;

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

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrantProgramName() {
        return grantProgramName;
    }

    public void setGrantProgramName(String grantProgramName) {
        this.grantProgramName = grantProgramName;
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
