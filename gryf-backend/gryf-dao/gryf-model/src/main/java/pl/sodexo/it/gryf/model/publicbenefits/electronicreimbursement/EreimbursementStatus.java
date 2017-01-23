package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Encja dla statusów rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "E_REIMBURSEMENT_STATUSES", schema = "APP_PBE")
public class EreimbursementStatus {

    public static final String NEW_ERMBS = "NEW";
    public static final String TO_ERMBS = "T_RMS";
    public static final String TO_CORRECT = "T_CRR";
    public static final String TO_VERIFY = "T_VRF";
    public static final String SETTLED = "REIMB";
    public static final String GENERATED_DOCUMENTS = "G_DOC";
    public static final String CANCELED = "CNCL";
    public static final String REJECTED = "RJCT";

    @Id
    @Column(name = "ID")
    @Getter
    @Setter
    private String id;

    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    @Column(name = "ORDINAL")
    @Getter
    @Setter
    private Integer ordinal;

}
