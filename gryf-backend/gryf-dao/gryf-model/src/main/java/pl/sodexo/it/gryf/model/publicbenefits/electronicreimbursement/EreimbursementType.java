package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Encja typów rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 19.12.2016.
 */
@ToString
@Entity
@Table(name = "E_REIMBURSEMENT_TYPES", schema = "APP_PBE")
public class EreimbursementType {

    public static final String TI_INST = "TI_INST";
    public static final String URSVD_POOL = "URSVD_POOL";

    @Id
    @Column(name = "CODE")
    @Getter
    @Setter
    private String code;

    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    @Column(name = "ORDINAL")
    @Getter
    @Setter
    private Integer ordinal;

}
