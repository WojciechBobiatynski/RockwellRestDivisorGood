package pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.detailsform;

import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.validation.publicbenefits.grantapplication.ValidationGroupApplyMandatoryApplication;
import pl.sodexo.it.gryf.validation.publicbenefits.grantapplication.ValidationGroupNewGrantApplication;
import pl.sodexo.it.gryf.validation.publicbenefits.grantapplication.ValidationGroupRejectApplication;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-25.
 */
public class GrantApplicationDTO {

    //STATIC FIELDS

    public static final String REJECTION_REASON_ATTR_NAME = "rejectionReason";

    public static final String REJECTION_REASON_EMPTY_ERROR_MSG = "Powód odrzucenia nie może być pusty";

    //FIELDS

    private Long id;

    @NotNull(message = "Program dofinansowania nie może być pusty", groups = ValidationGroupNewGrantApplication.class)
    private DictionaryDTO grantProgram;

    @NotNull(message = "Wersja wniosku nie może być pusta", groups = ValidationGroupNewGrantApplication.class)
    private GrantApplicationVersionDictionaryDTO applicationVersion;

    private DictionaryDTO status;

    private EnterpriseSearchResultDTO enterprise;

    @NotNull(message = "Data złożenia wniosku nie może być pusta", groups = ValidationGroupApplyMandatoryApplication.class)
    private Date applyDate;

    @NotNull(message = "Data rozpatrzenia wniosku nie może być pusta", groups = {ValidationGroupRejectApplication.class})
    private Date considerationDate;

    @NotEmpty(message = "Pole 'Powód odrzucenia' nie może być puste", groups = ValidationGroupRejectApplication.class)
    @Size(message = "Pole 'Powód odrzucenia' może mieć długość maksymalnie 1000 znaków", max = 1000, groups = ValidationGroupRejectApplication.class)
    private String rejectionReason;

    private Integer version;

    @Valid
    private List<GrantApplicationAttachmentDTO> attachments;

    @NotNull(message = "Pole 'Data wpłynięcia wniosku' nie może być puste", groups = ValidationGroupApplyMandatoryApplication.class)
    private Date receiptDate;

    //GETTERS & SETETRS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantApplicationVersionDictionaryDTO getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(GrantApplicationVersionDictionaryDTO applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public EnterpriseSearchResultDTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseSearchResultDTO enterprise) {
        this.enterprise = enterprise;
    }

    public DictionaryDTO getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(DictionaryDTO grantProgram) {
        this.grantProgram = grantProgram;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
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

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<GrantApplicationAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<GrantApplicationAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }
}


