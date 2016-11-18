package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Encja dla korekt rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "CORRECTIONS", schema = "APP_PBE")
@SequenceGenerator(name="correc_seq", schema = "eagle", sequenceName = "correc_seq", allocationSize = 1)
public class Correction {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_e_reimb_seq")
    @Getter
    @Setter
    private Long id;
}
