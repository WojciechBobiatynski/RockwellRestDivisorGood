package pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform;


import pl.sodexo.it.gryf.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-10.
 */
public class EnterpriseSearchResultDTO {

    //PRIVATE FIELDS

    private Long id;

    private String name;

    private String vatRegNum;

    private String addressInvoice;

    private ZipCodeSearchResultDTO zipCodeInvoice;

    private String addressCorr;

    private ZipCodeSearchResultDTO zipCodeCorr;

    //CONSTRUCTORS & CREATED LIST

    private EnterpriseSearchResultDTO(){
    }

    private EnterpriseSearchResultDTO(Enterprise entity) {
        this.setName(entity.getName());
        this.setVatRegNum(entity.getVatRegNum());
        this.setAddressInvoice(entity.getAddressInvoice());
        this.setZipCodeInvoice(ZipCodeSearchResultDTO.create(entity.getZipCodeInvoice()));
        this.setAddressCorr(entity.getAddressCorr());
        this.setZipCodeCorr(ZipCodeSearchResultDTO.create(entity.getZipCodeCorr()));
        this.setId(entity.getId());
    }

    //STATIC METHODS - CREATE

    public static EnterpriseSearchResultDTO create(Enterprise entity) {
        return entity != null ? new EnterpriseSearchResultDTO(entity) : null;
    }

    public static List<EnterpriseSearchResultDTO> createList(List<Enterprise> entities) {
        List<EnterpriseSearchResultDTO> list = new ArrayList<>();
        for (Enterprise entity : entities) {
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

