package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform;


import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import java.util.ArrayList;
import java.util.List;

public class IndividualSearchResultDTO {

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

    //CONSTRUCTORS & CREATED LIST

    private IndividualSearchResultDTO(){
    }

    private IndividualSearchResultDTO(Individual entity) {
        this.setFirstName(entity.getFirstName());
        this.setLastName(entity.getLastName());
        this.setPesel(entity.getPesel());
        this.setDocumentNumber(entity.getDocumentNumber());
        this.setDocumentType(entity.getDocumentType());
        this.setAddressInvoice(entity.getAddressInvoice());
        this.setZipCodeInvoice(ZipCodeSearchResultDTO.create(entity.getZipCodeInvoice()));
        this.setAddressCorr(entity.getAddressCorr());
        this.setZipCodeCorr(ZipCodeSearchResultDTO.create(entity.getZipCodeCorr()));
        this.setId(entity.getId());
    }

    //STATIC METHODS - CREATE

    public static IndividualSearchResultDTO create(Individual entity) {
        return entity != null ? new IndividualSearchResultDTO(entity) : null;
    }

    public static List<IndividualSearchResultDTO> createList(List<Individual> entities) {
        List<IndividualSearchResultDTO> list = new ArrayList<>();
        for (Individual entity : entities) {
            list.add(create(entity));
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

