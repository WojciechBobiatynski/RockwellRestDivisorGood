package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
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

    @OneToOne
    @JoinColumn(name = "TI_TR_INST_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private TrainingInstance trainingInstance;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private EreimbursementStatus ereimbursementStatus;

    @Column(name = "REIMBURSEMENT_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date reimbursementDate;

    @Column(name = "SXO_TI_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private BigDecimal sxoTiAmountDueTotal;

    @Column(name = "IND_TI_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private BigDecimal indTiAmountDueTotal;

    @Column(name = "TI_REIMB_ACCOUNT_NUMBER")
    @Getter
    @Setter
    private String tiReimbAccountNumber;

    @OneToMany(mappedBy = "ereimbursement")
    @Getter
    @Setter
    private List<ErmbsAttachment> ermbsAttachmentList;

}
