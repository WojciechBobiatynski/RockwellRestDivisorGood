package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementComplete;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementCorrect;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
public class ReimbursementDTO extends VersionableDto {

    //STATIC FIELDS - ATRIBUTES
    public static final String TRAINING_INSTITUTION_REIMBURSEMENT_ACCOUNT_NUMBER_ATTR_NAME = "trainingInstitutionReimbursementAccountNumber";
    public static final String INVOICE_ANON_GROSS_AMOUNT_ATTR_NAME = "invoiceAnonGrossAmount";
    public static final String REIMBURSEMENT_ATTACHMENTS_ATTR_NAME = "reimbursementAttachments";

    //FIELDS

    private Long id;

    private DictionaryDTO status;

    @NotNull(message = "Dostawa nie może być pusta")
    private ReimbursementDeliverySearchResultDTO reimbursementDelivery;

    @NotNull(message = "Data rozliczenia nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private Date reimbursementDate;

    @NotNull(message = "Dota utworzenia anonsu nie może być pusta")
    private Date announcementDate;

    @NotEmpty(message = "Numer faktury nie może być pusty", groups = ValidationGroupReimbursementSettleAndVerify.class)
    @Size(message = "Numer faktury może mieć maksymalnie 50 znaków", max = 50)
    private String invoiceNumber;

    @NotNull(message = "Kwota brutto faktury nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private BigDecimal invoiceAnonGrossAmount;

    @NotNull(message = "Kwota brutto rozliczenia bonami nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private BigDecimal invoiceAnonVouchAmount;

    @NotEmpty(message = "Numer rachunku bankowego do zwrotu dla IS nie może być puste", groups = ValidationGroupReimbursementSettleAndVerify.class)
    @Size(message = "Numer rachunku bankowego do zwrotu IS może mieć maksymalnie 26 znaków", max = 26)
    @Pattern(message = "Numer rachunku bankowego musi zawierać 26 cyfr", regexp = "^[0-9]{26}$")
    private String trainingInstitutionReimbursementAccountNumber;

    @NotNull(message = "Pole 'Usługodawca' nie może być puste", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private EnterpriseSearchResultDTO enterprise;

    private String createdUser;

    private BigDecimal sxoTiAmountDueTotal;

    private BigDecimal sxoEntAmountDueTotal;

    @NotNull(message = "Data przelewu nie może być pusta", groups = ValidationGroupReimbursementComplete.class)
    private Date transferDate;

    /**
     * Kwota nalezna IS do MSP
     */
    private BigDecimal entToTiAmountDueTotal;

    /**
     * Wykorzystany wkład własny MSP
     */
    private BigDecimal usedOwnEntContributionAmountTotal;

    /**
     * Wartość dofinansowania MSP
     */
    private BigDecimal grantAmountTotal;

    /**
     * Wartosć dofinansowania wpłąconego do IS
     */
    private BigDecimal grantAmountPayedToTiTotal;

    /**
     * Całkowity koszt usług
     */
    private BigDecimal trainingCostTotal;

    @Size(message = "Uwagi mogą mieć maksymalnie 1000 znaków", max = 1000)
    private String remarks;

    private Date requiredCorrectionDate;

    private Date correctionDate;

    private Integer correctionsNumber;

    @NotNull(message = "Powód korekty nie może być pusty", groups = ValidationGroupReimbursementCorrect.class)
    @Size(message = "powód korekty może mieć maksymalnie 1000 znaków", max = 1000)
    private String correctionReason;

    @Valid
    private List<ReimbursementTrainingDTO> reimbursementTrainings;

    @Valid
    private List<ReimbursementAttachmentDTO> reimbursementAttachments;

    /**
     * Wymagane załaczniki dla użytkowników. POle to jest wykorzystywane w GUI przy tworzeniu załaczników.
     */
    private List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachmentRequiredList;

    private Integer version;

    //PUBLIC METHODS

    public List<FileDTO> getAllFiles() {
        List<FileDTO> result = new ArrayList<>();
        if (getReimbursementAttachments() != null) {
            for (ReimbursementAttachmentDTO attachment : getReimbursementAttachments()) {
                if (attachment.getFile() != null) {
                    result.add(attachment.getFile());
                }
            }
        }
        if (getReimbursementTrainings() != null) {
            for (ReimbursementTrainingDTO training : getReimbursementTrainings()) {
                if (training.getReimbursementTrainees() != null) {
                    for (ReimbursementTraineeDTO trainee : training.getReimbursementTrainees()) {
                        if (trainee.getReimbursementTraineeAttachments() != null) {
                            for (ReimbursementTraineeAttachmentDTO attachment : trainee.getReimbursementTraineeAttachments()) {
                                if (attachment.getFile() != null) {
                                    result.add(attachment.getFile());
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
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

    public ReimbursementDeliverySearchResultDTO getReimbursementDelivery() {
        return reimbursementDelivery;
    }

    public void setReimbursementDelivery(ReimbursementDeliverySearchResultDTO reimbursementDelivery) {
        this.reimbursementDelivery = reimbursementDelivery;
    }

    public Date getReimbursementDate() {
        return reimbursementDate;
    }

    public void setReimbursementDate(Date reimbursementDate) {
        this.reimbursementDate = reimbursementDate;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getInvoiceAnonGrossAmount() {
        return invoiceAnonGrossAmount;
    }

    public void setInvoiceAnonGrossAmount(BigDecimal invoiceAnonGrossAmount) {
        this.invoiceAnonGrossAmount = invoiceAnonGrossAmount;
    }

    public BigDecimal getInvoiceAnonVouchAmount() {
        return invoiceAnonVouchAmount;
    }

    public void setInvoiceAnonVouchAmount(BigDecimal invoiceAnonVouchAmount) {
        this.invoiceAnonVouchAmount = invoiceAnonVouchAmount;
    }

    public String getTrainingInstitutionReimbursementAccountNumber() {
        return trainingInstitutionReimbursementAccountNumber;
    }

    public void setTrainingInstitutionReimbursementAccountNumber(String trainingInstitutionReimbursementAccountNumber) {
        this.trainingInstitutionReimbursementAccountNumber = trainingInstitutionReimbursementAccountNumber;
    }

    public EnterpriseSearchResultDTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseSearchResultDTO enterprise) {
        this.enterprise = enterprise;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public BigDecimal getSxoTiAmountDueTotal() {
        return sxoTiAmountDueTotal;
    }

    public void setSxoTiAmountDueTotal(BigDecimal sxoTiAmountDueTotal) {
        this.sxoTiAmountDueTotal = sxoTiAmountDueTotal;
    }

    public BigDecimal getSxoEntAmountDueTotal() {
        return sxoEntAmountDueTotal;
    }

    public void setSxoEntAmountDueTotal(BigDecimal sxoEntAmountDueTotal) {
        this.sxoEntAmountDueTotal = sxoEntAmountDueTotal;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public BigDecimal getEntToTiAmountDueTotal() {
        return entToTiAmountDueTotal;
    }

    public void setEntToTiAmountDueTotal(BigDecimal entToTiAmountDueTotal) {
        this.entToTiAmountDueTotal = entToTiAmountDueTotal;
    }

    public BigDecimal getUsedOwnEntContributionAmountTotal() {
        return usedOwnEntContributionAmountTotal;
    }

    public void setUsedOwnEntContributionAmountTotal(BigDecimal usedOwnEntContributionAmountTotal) {
        this.usedOwnEntContributionAmountTotal = usedOwnEntContributionAmountTotal;
    }

    public BigDecimal getGrantAmountTotal() {
        return grantAmountTotal;
    }

    public void setGrantAmountTotal(BigDecimal grantAmountTotal) {
        this.grantAmountTotal = grantAmountTotal;
    }

    public BigDecimal getGrantAmountPayedToTiTotal() {
        return grantAmountPayedToTiTotal;
    }

    public void setGrantAmountPayedToTiTotal(BigDecimal grantAmountPayedToTiTotal) {
        this.grantAmountPayedToTiTotal = grantAmountPayedToTiTotal;
    }

    public BigDecimal getTrainingCostTotal() {
        return trainingCostTotal;
    }

    public void setTrainingCostTotal(BigDecimal trainingCostTotal) {
        this.trainingCostTotal = trainingCostTotal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getRequiredCorrectionDate() {
        return requiredCorrectionDate;
    }

    public void setRequiredCorrectionDate(Date requiredCorrectionDate) {
        this.requiredCorrectionDate = requiredCorrectionDate;
    }

    public Date getCorrectionDate() {
        return correctionDate;
    }

    public void setCorrectionDate(Date correctionDate) {
        this.correctionDate = correctionDate;
    }

    public Integer getCorrectionsNumber() {
        return correctionsNumber;
    }

    public void setCorrectionsNumber(Integer correctionsNumber) {
        this.correctionsNumber = correctionsNumber;
    }

    public String getCorrectionReason() {
        return correctionReason;
    }

    public void setCorrectionReason(String correctionReason) {
        this.correctionReason = correctionReason;
    }

    public List<ReimbursementTrainingDTO> getReimbursementTrainings() {
        return reimbursementTrainings;
    }

    public void setReimbursementTrainings(List<ReimbursementTrainingDTO> reimbursementTrainings) {
        this.reimbursementTrainings = reimbursementTrainings;
    }

    public List<ReimbursementAttachmentDTO> getReimbursementAttachments() {
        return reimbursementAttachments;
    }

    public void setReimbursementAttachments(List<ReimbursementAttachmentDTO> reimbursementAttachments) {
        this.reimbursementAttachments = reimbursementAttachments;
    }

    public List<ReimbursementTraineeAttachmentDTO> getReimbursementTraineeAttachmentRequiredList() {
        return reimbursementTraineeAttachmentRequiredList;
    }

    public void setReimbursementTraineeAttachmentRequiredList(List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachmentRequiredList) {
        this.reimbursementTraineeAttachmentRequiredList = reimbursementTraineeAttachmentRequiredList;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
