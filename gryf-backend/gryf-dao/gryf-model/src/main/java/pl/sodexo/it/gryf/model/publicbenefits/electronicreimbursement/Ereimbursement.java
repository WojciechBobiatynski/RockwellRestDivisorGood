package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePool;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Encja dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "E_REIMBURSEMENTS", schema = "APP_PBE")
@SequenceGenerator(name = "pbe_e_reimb_seq", schema = "eagle", sequenceName = "pbe_e_reimb_seq", allocationSize = 1)
@OptimisticLocking(cascade = true)
@NamedQueries({
        @NamedQuery(name = "Ereimbursement.isInUserInstitution", query = "select count(e) "
                + "from Ereimbursement e join e.trainingInstance tins join tins.training t join t.trainingInstitution ti "
                + "join ti.trainingInstitutionUsers tiu "
                + "where e.id = :ereimbursementId and lower(tiu.login) = lower(:tiUserLogin)")})
public class Ereimbursement extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_e_reimb_seq")
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "CODE")
    @Getter
    @Setter
    private EreimbursementType ereimbursementType;

    @OneToOne
    @JoinColumn(name = "TI_TR_INST_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private TrainingInstance trainingInstance;

    @JoinColumn(name = "PRODUCT_INSTANCE_POOL_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    @ManyToOne
    private PbeProductInstancePool productInstancePool;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private EreimbursementStatus ereimbursementStatus;

    @Column(name = "ARRIVAL_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date arrivalDate;

    @Column(name = "REIMBURSEMENT_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date reimbursementDate;

    @Column(name = "RECON_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date reconDate;

    @Column(name = "SXO_TI_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private BigDecimal sxoTiAmountDueTotal;

    @Column(name = "SXO_IND_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private BigDecimal sxoIndAmountDueTotal;

    @Column(name = "IND_TI_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private BigDecimal indTiAmountDueTotal;

    @Column(name = "IND_OWN_CONTRIBUTION_USED")
    @Getter
    @Setter
    private BigDecimal indOwnContributionUsed;

    @Column(name = "IND_SUBSIDY_VALUE")
    @Getter
    @Setter
    private BigDecimal indSubsidyValue;

    @Column(name = "TI_REIMB_ACCOUNT_NUMBER")
    @Getter
    @Setter
    private String tiReimbAccountNumber;

    @Column(name = "REQUIRED_CORRECTION_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date requiredCorrectionDate;

    //TODO: zmienić nazwę kolumnt na PRODUCTS_NUM (ze względu na nowe rozliczenia)
    @Column(name = "EXPIRED_PRODUCTS_NUM")
    @Getter
    @Setter
    private Integer expiredProductsNum;

    @Column(name = "REJECTION_REASON_ID")
    @Getter
    @Setter
    private Long rejectionReasonId;

    @Column(name = "REJECTION_DETAILS")
    @Getter
    @Setter
    private String rejectionDetails;

    @Column(name = "REJECTION_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date rejectionDate;

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<ErmbsAttachment> ermbsAttachmentList;

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<EreimbursementReport> ereimbursementReports;

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<EreimbursementMail> ereimbursementMails;

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<EreimbursementInvoice> ereimbursementInvoices;

}
