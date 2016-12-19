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

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<ErmbsAttachment> ermbsAttachmentList;

}
