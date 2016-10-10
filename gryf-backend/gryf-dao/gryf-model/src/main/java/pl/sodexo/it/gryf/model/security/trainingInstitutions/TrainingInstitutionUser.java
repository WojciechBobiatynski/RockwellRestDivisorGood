package pl.sodexo.it.gryf.model.security.trainingInstitutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import javax.persistence.*;
import java.util.Date;

/**
 * Encja reprezentująca użytkowników instytucji szkoleniowych
 *
 * Created by jbentyn on 2016-10-06.
 */
@Entity
@Table(name = "TRAINING_INSTITUTION_USERS", schema = "APP_PBE")
@SequenceGenerator(name = "ti_usr_seq", schema = "eagle", sequenceName = "ti_usr_seq", allocationSize = 1)
@ToString(exclude = {"password", "trainingInstitution"})
public class TrainingInstitutionUser extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_usr_seq")
    @Getter
    @Setter
    private Long id;

    @Column(name = "LOGIN", nullable = false, length = 150)
    @Getter
    @Setter
    private String login;

    @Column(name = "EMAIL", nullable = false, length = 150)
    @Getter
    @Setter
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    @Getter
    @Setter
    private String password;

    @Column(name = "LAST_LOGIN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastLoginDate;

    @Column(name = "PASSWORD_EXP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date passwordExpirationDate;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Getter
    @Setter
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINING_ISTITUTION_ID")
    @Getter
    @Setter
    private TrainingInstitution trainingInstitution;
}
