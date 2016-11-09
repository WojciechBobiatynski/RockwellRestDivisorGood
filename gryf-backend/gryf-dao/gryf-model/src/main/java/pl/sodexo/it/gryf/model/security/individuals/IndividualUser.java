package pl.sodexo.it.gryf.model.security.individuals;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.security.TeRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Encja reprezentuająca użytkownika osoby fizycznej
 *
 * Created by akmiecinski on 19.10.2016.
 */
@Entity
@Table(name = "INDIVIDUAL_USERS", schema = "APP_PBE")
@SequenceGenerator(name = "ind_usr_seq", schema = "eagle", sequenceName = "ind_usr_seq", allocationSize = 1)
@ToString(exclude = {"verificationCode", "individual"})
public class IndividualUser extends VersionableEntity {

    @Id
    @Column(name = "INU_ID")
    @GeneratedValue(generator = "ind_usr_seq")
    @Getter
    @Setter
    private Long inuId;

    @OneToOne
    @JoinColumn(name = "IND_ID")
    @Getter
    @Setter
    private Individual individual;

    @Column(name = "VERIFICATION_CODE")
    @Getter
    @Setter
    private String verificationCode;

    @Column(name = "IS_ACTIVE")
    @Getter
    @Setter
    private boolean active;

    @Column(name = "LAST_LOGIN_SUCCESS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastLoginSuccessDate;

    @Column(name = "LAST_LOGIN_FAILURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastLoginFailureDate;

    @Column(name = "LOGIN_FAILURE_ATTEMPTS")
    @Getter
    @Setter
    private Integer loginFailureAttempts = 0;

    @Column(name = "LAST_RESET_FAILURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date lastResetFailureDate;

    @Column(name = "RESET_FAILURE_ATTEMPTS")
    @Getter
    @Setter
    private Integer resetFailureAttempts = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TE_IND_USER_IN_ROLES", schema = "APP_PBE", joinColumns = {
            @JoinColumn(name = "INU_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TE_ROLE_CODE", referencedColumnName = "CODE")})
    @Getter
    @Setter
    private List<TeRole> roles = new ArrayList<>();

}
