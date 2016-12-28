package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform;

import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@ToString
public class GrantApplicationSearchQueryDTO extends SearchDto {

    //FIELDS

    private Long id;

    private String statusId;

    private Long enterpriseId;

    private String enterpriseName;

    private String vatRegNum;

    private String addressInvoice;

    private String zipCodeInvoiceCode;

    private String zipCodeInvoiceCity;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date applyDateFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date applyDateTo;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date considerationDateFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date considerationDateTo;

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

    public String getZipCodeInvoiceCode() {
        return zipCodeInvoiceCode;
    }

    public void setZipCodeInvoiceCode(String zipCodeInvoiceCode) {
        this.zipCodeInvoiceCode = zipCodeInvoiceCode;
    }

    public String getZipCodeInvoiceCity() {
        return zipCodeInvoiceCity;
    }

    public void setZipCodeInvoiceCity(String zipCodeInvoiceCity) {
        this.zipCodeInvoiceCity = zipCodeInvoiceCity;
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

    public String getAddressInvoice() {
        return addressInvoice;
    }

    public void setAddressInvoice(String addressInvoice) {
        this.addressInvoice = addressInvoice;
    }

    public Date getApplyDateFrom() {
        return applyDateFrom;
    }

    public Date getApplyDateTo() {
        return applyDateTo;
    }

    public void setApplyDateTo(Date applyDateTo) {
        this.applyDateTo = applyDateTo;
    }

    public void setApplyDateFrom(Date applyDateFrom) {
        this.applyDateFrom = applyDateFrom;
    }

    public Date getConsiderationDateFrom() {
        return considerationDateFrom;
    }

    public void setConsiderationDateFrom(Date considerationDateFrom) {
        this.considerationDateFrom = considerationDateFrom;
    }

    public Date getConsiderationDateTo() {
        return considerationDateTo;
    }

    public void setConsiderationDateTo(Date considerationDateTo) {
        this.considerationDateTo = considerationDateTo;
    }
}
