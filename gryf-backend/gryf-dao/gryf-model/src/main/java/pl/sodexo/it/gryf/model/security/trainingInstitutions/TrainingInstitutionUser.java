package pl.sodexo.it.gryf.model.security.trainingInstitutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.security.TeRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Encja reprezentująca użytkowników usługodawcy szkoleniowych
 *
 * Created by jbentyn on 2016-10-06.
 */
@Entity
@Table(name = "TRAINING_INSTITUTION_USERS", schema = "APP_PBE")
@SequenceGenerator(name = "ti_usr_seq", schema = "eagle", sequenceName = "ti_usr_seq", allocationSize = 1)
@ToString(exclude = {"password", "trainingInstitution", "tiUserResetAttemptList", "roles"})
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

    @Column(name = "LAST_LOGIN_SUCCESS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastLoginSuccessDate;

    @Column(name = "PASSWORD_EXP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date passwordExpirationDate;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Getter
    @Setter
    private Boolean isActive = true;

    @Column(name = "LAST_LOGIN_FAILURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastLoginFailureDate;

    @Column(name = "LOGIN_FAILURE_ATTEMPTS")
    @Getter
    @Setter
    private Integer loginFailureAttempts = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINING_ISTITUTION_ID")
    @Getter
    @Setter
    private TrainingInstitution trainingInstitution;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingInstitutionUser")
    @Getter
    @Setter
    private List<TiUserResetAttempt> tiUserResetAttemptList;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TI_USER_IN_ROLES", schema = "APP_PBE", joinColumns = {
            @JoinColumn(name = "TI_USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TE_ROLE_CODE", referencedColumnName = "CODE")})
    @Getter
    @Setter
    private List<TeRole> roles = new ArrayList<>();

}
