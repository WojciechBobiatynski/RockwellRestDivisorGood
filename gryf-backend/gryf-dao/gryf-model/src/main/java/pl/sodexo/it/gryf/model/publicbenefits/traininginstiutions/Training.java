package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.BooleanConverter;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training.QUERY_FIND_BY_EXTERNAL_ID_AND_PROGRAM_ID;
import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training.QUERY_TRAINING_DEACTIVATE_TRAININGS;

/**
 * Encja reprezentuająca usługa
 *
 * Created by kantczak on 2016-10-26.
 */
@Entity
@Table(name = "TI_TRAININGS", schema = "APP_PBE")
@SequenceGenerator(name = "ti_tra_seq", schema = "eagle", sequenceName = "ti_tra_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = QUERY_FIND_BY_EXTERNAL_ID_AND_PROGRAM_ID, query = "select e from Training e where e.externalId = :externalId and e.grantProgram.id = :grantProgramId"),
        @NamedQuery(name = QUERY_TRAINING_DEACTIVATE_TRAININGS, query = "update Training tt set tt.active = false, tt.deactivateDate = CURRENT_TIMESTAMP, "
                + "tt.deactivateJob = :importJob, tt.version = (tt.version + 1), tt.modifiedTimestamp = CURRENT_TIMESTAMP, tt.modifiedUser = :modifiedUser "
                + "where tt.active = true " +
                "   and tt.id not in "
                + "(select r.training.id from ImportDataRow r " +
                "     where  r.training.id is not null and r.importJob = :importJob" +
                "            AND r.importJob.type.grantProgramId = :grantProgramId " +
                "  )" +
                "   and tt.grantProgram.id  = :grantProgramId" /* Szkolenia pochadza z tego samego programu co importowany*/
                ),
        @NamedQuery(name = "Training.isInUserInstitution", query = "select count(e) "
                + "from Training e join e.trainingInstitution ti "
                + "join ti.trainingInstitutionUsers tiu "
                + "where e.id = :trainingId and lower(tiu.login) = lower(:tiUserLogin)"),

})
@ToString
@Getter
@Setter
public class Training extends VersionableEntity {

    public static final String END_DATE_ATTR_NAME = "endDate";

    public static final String QUERY_TRAINING_DEACTIVATE_TRAININGS = "Training.deactiveTrainings";
    public static final String QUERY_FIND_BY_EXTERNAL_ID_AND_PROGRAM_ID = "Training.findByExternalIdAndProgramId";

    public static final String PARAMETER_GRANT_PROGRAM_ID = "grantProgramId";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_tra_seq")
    private Long id;

    @Column(name = "EXTERNAL_ID")
    @Size(max = 30, message = "Idnetyfikator zewnętrzny usługi musi zawierać maksymalnie 30 znaków")
    private String externalId;

    @OneToOne
    @JoinColumn(name = "TRAINING_INSTITUTION_ID")
    @NotNull(message = "Należy wybrać usługodawcę")
    private TrainingInstitution trainingInstitution;

    @Column(name = "NAME")
    @NotEmpty(message = "Nazwa usługi nie może być pusta")
    @Size(max = 200, message = "Nazwa usługi musi zawierać maksymalnie 200 znaków")
    private String name;

    @Column(name = "PRICE")
    @NotNull(message = "Należy podać cenę usługi")
    private BigDecimal price;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Należy podać datę rozpoczęcia usługi")
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Należy podać datę zakończenia usługi")
    private Date endDate;

    @Column(name = "PLACE")
    @NotEmpty(message = "Należy podać miejsce usługi")
    @Size(max = 200, message = "Miejsce usługi musi zawierać maksymalnie 200 znaków")
    private String place;

    @Column(name = "HOURS_NUMBER")
    private Integer hoursNumber;

    @Column(name = "HOUR_PRICE")
    private BigDecimal hourPrice;

    @OneToOne
    @JoinColumn(name = "TRAINING_CATEGORY_ID")
    @NotNull(message = "Należy wybrać kategorię usługi")
    private TrainingCategory category;

    @Column(name = "REIMBURSMENT_CONDITIONS")
    @Size(max = 1000, message = "Warunki usługi muszą zawierać maksymalnie 1000 znaków")
    private String reimbursmentConditions;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "DEACTIVATE_USER")
    @Size(max = 100, message = "Uzytkownik deaktywujący usługa musi zawierać maksymalnie 100 znaków")
    private String deactivateUser;

    @Column(name = "DEACTIVATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deactivateDate;

    @ManyToOne
    @JoinColumn(name = "DEACTIVATE_JOB_ID")
    private AsynchronizeJob deactivateJob;

    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    @ManyToOne
    @NotNull(message = "Usługa nie ma przypisanego programu/projektu")
    private GrantProgram grantProgram;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "INDIVIDUAL")
    private boolean isIndividual;

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((Training) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
