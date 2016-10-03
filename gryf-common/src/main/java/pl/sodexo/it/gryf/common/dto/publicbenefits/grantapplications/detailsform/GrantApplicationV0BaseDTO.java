package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.common.validation.VatRegNumFormat;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.ValidationGroupApplyOptionalApplication;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-25.
 */
@ToString
public class GrantApplicationV0BaseDTO extends GrantApplicationDTO {

    //FIELDS

    @NotEmpty(message = "Nazwa przedsiębiorstwa nie może być pusta", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Nazwa przedsiębiorstwa noże mieć maksymalnie 500 znaków", max = 500)
    private String enterpriseName;

    @VatRegNumFormat(message = "Błedny format NIP", groups = ValidationGroupApplyOptionalApplication.class)
    @NotEmpty(message = "Numer NIP nie może być pusty", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Numer NIP noże mieć maksymalnie 20 znaków", max = 20)
    private String vatRegNum;

    private DictionaryDTO entityType;

    @NotEmpty(message = "Adres do faktury nie może być pusty", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Adres do faktury może mieć maksymalnie 200 znaków", max = 200)
    private String addressInvoice;

    @NotNull(message = "Kod do faktury nie może być pusty", groups = ValidationGroupApplyOptionalApplication.class)
    private ZipCodeSearchResultDTO zipCodeInvoice;

    @Size(message = "Powiat noże mieć maksymalnie 50 znaków", max = 50)
    private String county;

    @NotNull(message = "Wielkość przedsiębiorstwa nie może być pusta", groups = ValidationGroupApplyOptionalApplication.class)
    private DictionaryDTO enterpriseSize;

    @NotNull(message = "Ilość wnioskowanych kuponów nie może być pusta", groups = ValidationGroupApplyOptionalApplication.class)
    private Integer vouchersNumber;

    @Size(message = "Adres do korespondencji może mieć maksymalnie 200 znaków", max = 200)
    private String addressCorr;

    private ZipCodeSearchResultDTO zipCodeCorr;

    @NotEmpty(message = "Rachunek nie może być pusty", groups = ValidationGroupApplyOptionalApplication.class)
    @Pattern(message = "Numer bankowy musi zawierać 26 cyfr", regexp = "^[0-9]{26}$")
    private String accountRepayment;

    @Valid
    private List<GrantApplicationPkdDTO> pkdList;

    @Valid
    private List<GrantApplicationContactDataDTO> contacts;

    @Valid
    @Size(message = "Należy dodać minimum dwie osoby do odbioru bonów", min = 2, groups = ValidationGroupApplyOptionalApplication.class)
    private List<GrantApplicationContactDataDTO> deliveryContacts;

    //PUBLIC METHODS

    @JsonIgnore
    @AssertTrue(message = "Adres oraz kod pocztowy do korespondencji muszą być jednocześnie wypełnione lub puste" )
    public boolean isCorrSameFill(){
        return isCorrBothFill() || isCorrBothEmpty();
    }

    @JsonIgnore
    public boolean isCorrBothFill(){
        return (!StringUtils.isEmpty(addressCorr) && zipCodeCorr != null);
    }

    @JsonIgnore
    public boolean isCorrBothEmpty(){
        return (StringUtils.isEmpty(addressCorr) && zipCodeCorr == null);
    }

    //GETTERS & SETTERS

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

    public DictionaryDTO getEntityType() {
        return entityType;
    }

    public void setEntityType(DictionaryDTO entityType) {
        this.entityType = entityType;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public DictionaryDTO getEnterpriseSize() {
        return enterpriseSize;
    }

    public void setEnterpriseSize(DictionaryDTO enterpriseSize) {
        this.enterpriseSize = enterpriseSize;
    }

    public Integer getVouchersNumber() {
        return vouchersNumber;
    }

    public void setVouchersNumber(Integer vouchersNumber) {
        this.vouchersNumber = vouchersNumber;
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

    public String getAccountRepayment() {
        return accountRepayment;
    }

    public void setAccountRepayment(String accountRepayment) {
        this.accountRepayment = accountRepayment;
    }

    public List<GrantApplicationPkdDTO> getPkdList() {
        return pkdList;
    }

    public void setPkdList(List<GrantApplicationPkdDTO> pkdList) {
        this.pkdList = pkdList;
    }

    public List<GrantApplicationContactDataDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<GrantApplicationContactDataDTO> contacts) {
        this.contacts = contacts;
    }

    public List<GrantApplicationContactDataDTO> getDeliveryContacts() {
        return deliveryContacts;
    }

    public void setDeliveryContacts(List<GrantApplicationContactDataDTO> deliveryContacts) {
        this.deliveryContacts = deliveryContacts;
    }

}
