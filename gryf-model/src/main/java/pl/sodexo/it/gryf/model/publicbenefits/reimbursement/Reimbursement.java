/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"reimbursementDelivery", "status", "enterprise", "reimbursementAttachments", "reimbursementTrainings"})
@Entity
@Table(name = "REIMBURSEMENTS", schema = "APP_PBE")
@SequenceGenerator(name="pbe_reimb_seq", schema = "eagle", sequenceName = "pbe_reimb_seq", allocationSize = 1)
@OptimisticLocking(cascade=true)
public class Reimbursement extends VersionableEntity {

    //STATIC FEILDS - STATUSES CODE
    public static final String ID_ATTR_NAME = "id";
    public static final String INVOICE_NUMBER_ATTR_NAME = "invoiceNumber";
    public static final String STATUS_ATTR_NAME = "status";
    public static final String REIMBURSEMENT_DELIVERY_ATTR_NAME = "reimbursementDelivery";
    public static final String ANNOUNCEMENT_DATE_ATTR_NAME = "announcementDate";
    public static final String REIMBURSEMENT_DATE_ATTR_NAME = "reimbursementDate";

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_reimb_seq")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_DELIVERY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementDelivery reimbursementDelivery;

    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID")
    @ManyToOne(optional = false)
    private ReimbursementStatus status;

    @NotNull
    @Column(name = "ANNOUNCEMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date announcementDate;

    @Column(name = "REIMBURSEMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reimbursementDate;

    @Size(max = 50)
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Size(max = 26)
    @Column(name = "TI_REIMB_ACCOUNT_NUMBER")
    private String trainingInstitutionReimbursementAccountNumber;

    @Column(name = "INVOICE_ANON_GROSS_AMOUNT")
    private BigDecimal invoiceAnonGrossAmount;

    @Column(name = "INOVOICE_ANON_VOUCH_AMOUNT")
    private BigDecimal invoiceAnonVouchAmount;

    @JoinColumn(name = "ENTERPRISE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Enterprise enterprise;

    @Column(name = "REQUIRED_CORRECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requiredCorrectionDate;

    @Size(max = 1000)
    @Column(name = "CORRECTION_REASON")
    private String correctionReason;

    @Column(name = "CORRECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date correctionDate;

    @Column(name = "CORRECTIONS_NUMBER")
    private Integer correctionsNumber;

    @Column(name = "SXO_TI_AMOUNT_DUE_TOTAL")
    private BigDecimal sxoTiAmountDueTotal;

    @Column(name = "SXO_ENT_AMOUNT_DUE_TOTAL")
    private BigDecimal sxoEntAmountDueTotal;

    @Column(name = "TRANSFER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;

    @Size(max = 1000)
    @Column(name = "REMARKS")
    private String remarks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursement", orphanRemoval = true)
    private List<ReimbursementAttachment> reimbursementAttachments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursement", orphanRemoval = true)
    private List<ReimbursementTraining> reimbursementTrainings;

    //LIST METHODS

    private List<ReimbursementAttachment> getInitializedReimbursementAttachments() {
        if (reimbursementAttachments == null)
            reimbursementAttachments = new ArrayList<>();
        return reimbursementAttachments;
    }

    public List<ReimbursementAttachment> getReimbursementAttachments() {
        return Collections.unmodifiableList(getInitializedReimbursementAttachments());
    }

    public void addReimbursementAttachment(ReimbursementAttachment attachment) {
        if (attachment.getReimbursement() != null && attachment.getReimbursement() != this) {
            attachment.getReimbursement().getInitializedReimbursementAttachments().remove(attachment);
        }
        if (attachment.getId() == null || !getInitializedReimbursementAttachments().contains(attachment)) {
            getInitializedReimbursementAttachments().add(attachment);
        }
        attachment.setReimbursement(this);
    }

    public void removeReimbursementAttachment(ReimbursementAttachment attachment) {
        getInitializedReimbursementAttachments().remove(attachment);
    }

    private List<ReimbursementTraining> getInitializedReimbursementTrainings() {
        if (reimbursementTrainings == null)
            reimbursementTrainings = new ArrayList<>();
        return reimbursementTrainings;
    }

    public List<ReimbursementTraining> getReimbursementTrainings() {
        return Collections.unmodifiableList(getInitializedReimbursementTrainings());
    }

    public void addReimbursementTraining(ReimbursementTraining training) {
        if (training.getReimbursement() != null && training.getReimbursement() != this) {
            training.getReimbursement().getInitializedReimbursementTrainings().remove(training);
        }
        if (training.getId() == null || !getInitializedReimbursementTrainings().contains(training)) {
            getInitializedReimbursementTrainings().add(training);
        }
        training.setReimbursement(this);
    }

    public void removeReimbursementTraining(ReimbursementTraining training) {
        getInitializedReimbursementTrainings().remove(training);
    }


    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementDelivery getReimbursementDelivery() {
        return reimbursementDelivery;
    }

    public void setReimbursementDelivery(ReimbursementDelivery reimbursementDelivery) {
        this.reimbursementDelivery = reimbursementDelivery;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementStatus status) {
        this.status = status;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public Date getReimbursementDate() {
        return reimbursementDate;
    }

    public void setReimbursementDate(Date reimbursementDate) {
        this.reimbursementDate = reimbursementDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTrainingInstitutionReimbursementAccountNumber() {
        return trainingInstitutionReimbursementAccountNumber;
    }

    public void setTrainingInstitutionReimbursementAccountNumber(String trainingInstitutionReimbursementAccountNumber) {
        this.trainingInstitutionReimbursementAccountNumber = trainingInstitutionReimbursementAccountNumber;
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

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Date getRequiredCorrectionDate() {
        return requiredCorrectionDate;
    }

    public void setRequiredCorrectionDate(Date requiredCorrectionDate) {
        this.requiredCorrectionDate = requiredCorrectionDate;
    }

    public String getCorrectionReason() {
        return correctionReason;
    }

    public void setCorrectionReason(String correctionReason) {
        this.correctionReason = correctionReason;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((Reimbursement) o).id);
    }
}
