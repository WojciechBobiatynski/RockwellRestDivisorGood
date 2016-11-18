package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Encja dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "E_REIMBURSEMENTS", schema = "APP_PBE")
@SequenceGenerator(name="pbe_e_reimb_seq", schema = "eagle", sequenceName = "pbe_e_reimb_seq", allocationSize = 1)
@OptimisticLocking(cascade=true)
public class Ereimbursement extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_e_reimb_seq")
    @Getter
    @Setter
    private Long id;

    @OneToOne
    @JoinColumn(name = "TI_TR_INST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private TrainingInstance trainingInstance;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private EreimbursementStatus ereimbursementStatus;

    @Column(name = "REIMBURSEMENT_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date reimbursementDate;

    @Column(name = "SXO_IND_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private Integer sxoIndAmountDueTotal;

    @Column(name = "IND_SXO_AMOUNT_DUE_TOTAL")
    @Getter
    @Setter
    private Integer indSxoAmountDueTotal;

}
