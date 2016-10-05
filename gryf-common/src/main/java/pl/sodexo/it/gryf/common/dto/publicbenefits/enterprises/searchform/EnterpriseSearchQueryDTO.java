package pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

/**
 * Created by tomasz.bilski.ext on 2015-08-10.
 */
@ToString
public class EnterpriseSearchQueryDTO extends SearchDto {

    //FIELDS

    private Long id;

    private String name;

    private String vatRegNum;

    private String addressInvoice;

    private String zipCodeInvoiceCode;

    private String zipCodeInvoiceCity;

    private String addressCorr;

    private String zipCodeCorrCode;

    private String zipCodeCorrCity;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddressCorr() {
        return addressCorr;
    }

    public void setAddressCorr(String addressCorr) {
        this.addressCorr = addressCorr;
    }

    public String getZipCodeCorrCode() {
        return zipCodeCorrCode;
    }

    public void setZipCodeCorrCode(String zipCodeCorrCode) {
        this.zipCodeCorrCode = zipCodeCorrCode;
    }

    public String getZipCodeCorrCity() {
        return zipCodeCorrCity;
    }

    public void setZipCodeCorrCity(String zipCodeCorrCity) {
        this.zipCodeCorrCity = zipCodeCorrCity;
    }

}
