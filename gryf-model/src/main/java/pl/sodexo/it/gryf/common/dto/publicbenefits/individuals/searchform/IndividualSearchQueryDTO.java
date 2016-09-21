package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform;

import pl.sodexo.it.gryf.common.dto.SearchDto;

public class IndividualSearchQueryDTO extends SearchDto {

    //FIELDS

    private Long id;

    private String firstName;

    private String lastName;

    private String pesel;

    private String documentNumber;

    private String documentType;

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
