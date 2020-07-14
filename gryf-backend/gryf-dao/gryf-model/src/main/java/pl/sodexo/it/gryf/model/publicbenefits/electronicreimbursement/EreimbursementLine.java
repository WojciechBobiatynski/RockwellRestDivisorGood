package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString(exclude = {"ereimbursement", "order"})
@Getter
@Setter
@Entity
@Table(name = "E_REIMBURSEMENT_LINES", schema = "APP_PBE")
@OptimisticLocking(cascade = true)
@NamedQueries({
        @NamedQuery(name = EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_DELETE_BY_RMB_ID, query = "delete from EreimbursementLine e " +
                " where e.ereimbursement = :ereimbursement"),
        @NamedQuery(name = EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_GET_AUDIT_BY_ID, query = "select new pl.sodexo.it.gryf.model.api.AuditableEntity(e.createdUser, e.createdTimestamp, e.modifiedUser, e.modifiedTimestamp) "
                + "from EreimbursementLine e "
                + "where e.id = :ereimbursementLineId"),
        @NamedQuery(name = EreimbursementLine.QUERY_E_REIMBURSEMENT_LINES_GET_LIST_BY_RMB_ID, query = "select e "
                + "from EreimbursementLine e "
                + "where e.ereimbursement = :ereimbursement")})
public class EreimbursementLine extends VersionableEntity {

    public static final String QUERY_E_REIMBURSEMENT_LINES_DELETE_BY_RMB_ID = "EreimbursementLine.deleteListByEreimbursementId";
    public static final String QUERY_E_REIMBURSEMENT_LINES_GET_AUDIT_BY_ID = "EreimbursementLine.getAuditableInfoById";
    public static final String QUERY_E_REIMBURSEMENT_LINES_GET_LIST_BY_RMB_ID = "EreimbursementLine.getListByEreimbursementId";

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_e_reimb_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "E_REIMBURSMENT_ID", referencedColumnName = "ID")
    private Ereimbursement ereimbursement;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private Order order;

    @Column(name = "USED_NUM")
    private Integer usedProductsNumber;

    @Column(name = "OWN_CONTRIBUTION_PERCENTAGE")
    private BigDecimal ownContributionPercentage;

    @Column(name = "IND_SUBSIDY_PERCENTAGE")
    private BigDecimal indSubsidyPercentage;

    @Column(name = "SXO_TI_AMOUNT_DUE_TOTAL")
    private BigDecimal sxoTiAmountDueTotal;

    @Column(name = "SXO_IND_AMOUNT_DUE_TOTAL")
    private BigDecimal sxoIndAmountDueTotal;

    @Column(name = "IND_OWN_CONTRIBUTION_USED")
    private BigDecimal indOwnContributionUsed;

    @Column(name = "IND_SUBSIDY_VALUE")
    private BigDecimal indSubsidyValue;
}
