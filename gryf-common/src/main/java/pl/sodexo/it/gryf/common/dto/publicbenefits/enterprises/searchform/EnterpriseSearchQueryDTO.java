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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EnterpriseSearchQueryDTO that = (EnterpriseSearchQueryDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (vatRegNum != null ? !vatRegNum.equals(that.vatRegNum) : that.vatRegNum != null)
            return false;
        if (addressInvoice != null ? !addressInvoice.equals(that.addressInvoice) : that.addressInvoice != null)
            return false;
        if (zipCodeInvoiceCode != null ? !zipCodeInvoiceCode.equals(that.zipCodeInvoiceCode) : that.zipCodeInvoiceCode != null)
            return false;
        if (zipCodeInvoiceCity != null ? !zipCodeInvoiceCity.equals(that.zipCodeInvoiceCity) : that.zipCodeInvoiceCity != null)
            return false;
        if (addressCorr != null ? !addressCorr.equals(that.addressCorr) : that.addressCorr != null)
            return false;
        if (zipCodeCorrCode != null ? !zipCodeCorrCode.equals(that.zipCodeCorrCode) : that.zipCodeCorrCode != null)
            return false;
        return zipCodeCorrCity != null ? zipCodeCorrCity.equals(that.zipCodeCorrCity) : that.zipCodeCorrCity == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vatRegNum != null ? vatRegNum.hashCode() : 0);
        result = 31 * result + (addressInvoice != null ? addressInvoice.hashCode() : 0);
        result = 31 * result + (zipCodeInvoiceCode != null ? zipCodeInvoiceCode.hashCode() : 0);
        result = 31 * result + (zipCodeInvoiceCity != null ? zipCodeInvoiceCity.hashCode() : 0);
        result = 31 * result + (addressCorr != null ? addressCorr.hashCode() : 0);
        result = 31 * result + (zipCodeCorrCode != null ? zipCodeCorrCode.hashCode() : 0);
        result = 31 * result + (zipCodeCorrCity != null ? zipCodeCorrCity.hashCode() : 0);
        return result;
    }
}
