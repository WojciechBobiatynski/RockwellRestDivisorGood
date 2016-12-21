package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-12-20.
 */
@Entity
@Table(name = "E_REIMBURSEMENT_REPORTS", schema = "APP_PBE")
@ToString
public class EreimbursementReport extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    private Long id;

    @JoinColumn(name = "E_REIMBURSEMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    @Getter
    @Setter
    private Ereimbursement ereimbursement;

    @Column(name = "TYPE_NAME")
    @Getter
    @Setter
    private String typeName;

    @Column(name = "FILE_LOCATION")
    @Getter
    @Setter
    private String fileLocation;
}
