package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@ToString
public class GrantApplicationSearchResultDTO {

    //FIELDS

    private Long id;

    private DictionaryDTO status;

    private Long enterpriseId;

    private String enterpriseName;

    private String vatRegNum;

    private String addressInvoice;

    private ZipCodeSearchResultDTO zipCodeInvoice;

    private Date applyDate;

    private Date considerationDate;

    //CONSTRUCTORS & CREATED LIST

    private GrantApplicationSearchResultDTO(){
    }

    private GrantApplicationSearchResultDTO(GrantApplication entity) {
        GrantApplicationBasicData basicData = entity.getBasicData();

        this.setId(entity.getId());
        this.setStatus(DictionaryDTO.create(entity.getStatus()));
        this.setEnterpriseId((entity.getEnterprise() != null) ? entity.getEnterprise().getId() : null);
        this.setEnterpriseName(basicData != null ? basicData.getEnterpriseName() : null);
        this.setVatRegNum(basicData != null ? basicData.getVatRegNum() : null);
        this.setAddressInvoice(basicData != null ? basicData.getAddressInvoice() : null);
        this.setZipCodeInvoice(ZipCodeSearchResultDTO.create(basicData != null ? basicData.getZipCodeInvoice() : null));
        this.setApplyDate(entity.getApplyDate());
        this.setConsiderationDate(entity.getConsiderationDate());
    }

    //STATIC METHODS - CREATE

    public static GrantApplicationSearchResultDTO create(GrantApplication entity) {
        return entity != null ? new GrantApplicationSearchResultDTO(entity) : null;
    }

    public static List<GrantApplicationSearchResultDTO> createList(List<GrantApplication> entities) {
        List<GrantApplicationSearchResultDTO> list = new ArrayList<>();
        for (GrantApplication entity : entities) {
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

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
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

    public ZipCodeSearchResultDTO getZipCodeInvoice() {
        return zipCodeInvoice;
    }

    public void setZipCodeInvoice(ZipCodeSearchResultDTO zipCodeInvoice) {
        this.zipCodeInvoice = zipCodeInvoice;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getConsiderationDate() {
        return considerationDate;
    }

    public void setConsiderationDate(Date considerationDate) {
        this.considerationDate = considerationDate;
    }
}
