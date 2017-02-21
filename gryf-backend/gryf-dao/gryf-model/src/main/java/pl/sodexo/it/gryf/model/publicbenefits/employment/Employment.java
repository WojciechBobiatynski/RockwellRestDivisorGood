package pl.sodexo.it.gryf.model.publicbenefits.employment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import javax.persistence.*;

/**
 * Encja dla zatrudnienia
 *
 * Created by akmiecinski on 28.10.2016.
 */
@Entity
@Table(name = "EMPLOYMENTS", schema = "APP_PBE")
@ToString
    @NamedQueries({
            @NamedQuery(name = "Employment.findByIndividualAndEnterpriseId",
            query = "select e from Employment e where e.individual.id = :individualId and e.enterprise.id = :enterpriseId"),
})
public class Employment extends VersionableEntity {

    @Id
    @Column(name = "EMP_ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    private Long empId;

    @ManyToOne
    @JoinColumn(name="IND_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Individual individual;

    @ManyToOne
    @JoinColumn(name="ENT_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Enterprise enterprise;

}
