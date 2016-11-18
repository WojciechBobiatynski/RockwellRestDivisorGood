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
