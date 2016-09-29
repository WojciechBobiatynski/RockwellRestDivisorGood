package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform;


import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import java.util.ArrayList;
import java.util.List;

@ToString
public class IndividualSearchResultDTO extends GryfDto{

    //PRIVATE FIELDS

    private Long id;

    private String firstName;

    private String lastName;

    private String pesel;

    private String documentNumber;

    private String documentType;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

