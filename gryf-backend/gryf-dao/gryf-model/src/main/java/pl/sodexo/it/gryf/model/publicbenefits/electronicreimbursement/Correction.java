package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Encja dla korekt rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "CORRECTIONS", schema = "APP_PBE")
@SequenceGenerator(name = "correc_seq", schema = "eagle", sequenceName = "correc_seq", allocationSize = 1)
public class Correction extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_e_reimb_seq")
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "E_RMBS_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Ereimbursement ereimbursement;

    @Column(name = "REASON")
    @Getter
    @Setter
    private String reason;

    @Column(name = "COMPLEMENT_DATE")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date complementDate;
}
