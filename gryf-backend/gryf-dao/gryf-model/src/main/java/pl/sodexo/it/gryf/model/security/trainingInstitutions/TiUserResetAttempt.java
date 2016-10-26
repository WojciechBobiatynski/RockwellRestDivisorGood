package pl.sodexo.it.gryf.model.security.trainingInstitutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Encja reprezentująca żądania użytkonwików o reset hasła
 *
 * Created by akmiecinski on 26.10.2016.
 */
@Entity
@Table(name = "TRAINING_INSTITUTION_USERS", schema = "APP_PBE")
@ToString(exclude = {"trainingInstitutionUser"})
public class TiUserResetAttempt extends VersionableEntity {

    @Id
    @Column(name = "TUR_ID")
    @Getter
    @Setter
    private Long turId;

    @ManyToOne
    @JoinColumn(name="TIU_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private TrainingInstitutionUser trainingInstitutionUser;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date expiryDate;

    @Column(name = "USED")
    @Getter
    @Setter
    private boolean used;

}
