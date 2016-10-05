package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;
import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchResultDTO;

/**
 * Created by tomasz.bilski.ext on 2015-08-10.
 */
@ToString
public class TrainingInstitutionSearchResultDTO extends GryfDto {

    //PRIVATE FIELDS

    private Long id;

    private String name;

    private String vatRegNum;

    private String addressInvoice;

    private ZipCodeSearchResultDTO zipCodeInvoice;

    private String addressCorr;

    private ZipCodeSearchResultDTO zipCodeCorr;

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

    public ZipCodeSearchResultDTO getZipCodeInvoice() {
        return zipCodeInvoice;
    }

    public void setZipCodeInvoice(ZipCodeSearchResultDTO zipCodeInvoice) {
        this.zipCodeInvoice = zipCodeInvoice;
    }

    public String getAddressCorr() {
        return addressCorr;
    }

    public void setAddressCorr(String addressCorr) {
        this.addressCorr = addressCorr;
    }

    public ZipCodeSearchResultDTO getZipCodeCorr() {
        return zipCodeCorr;
    }

    public void setZipCodeCorr(ZipCodeSearchResultDTO zipCodeCorr) {
        this.zipCodeCorr = zipCodeCorr;
    }
}

