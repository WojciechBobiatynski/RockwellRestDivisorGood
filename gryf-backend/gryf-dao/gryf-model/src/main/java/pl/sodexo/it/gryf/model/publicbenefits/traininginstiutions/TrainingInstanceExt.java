package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt.QUERY_TRAINING_INSTANCE_EXT_DELETE_ALL_TRAINING_INSTANCE_EXT;


/**
 * Wpisy z pliku importu usług
 * <p>
 * Created by krzysztof.krawczynsk on 2018-03-13.
 */
@Entity
@Table(name = "TI_TRAINING_INSTANCES_EXT", schema = "APP_PBE")
@SequenceGenerator(name = "ti_tra_ins_ext_seq", schema = "eagle", sequenceName = "ti_tra_ins_ext_seq", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = QUERY_TRAINING_INSTANCE_EXT_DELETE_ALL_TRAINING_INSTANCE_EXT, query = "delete from TrainingInstanceExt t " +
                " where t.importJobId < :importJobId " +
                " and t.importJobId in (select aj.id from AsynchronizeJob  aj  " +
                "                        where  aj.type.grantProgramId = :grantProgramId )"),
        @NamedQuery(name = "TrainingInstanceExt.findByIndOrderExternalId", query = "select count(t) from TrainingInstanceExt t where UPPER(t.indOrderExternalId) like UPPER(CONCAT('%',:externalOrderId,'%')) "),
})
@ToString
@Getter
@Setter
public class TrainingInstanceExt extends VersionableEntity {

    public static final String END_DATE_ATTR_NAME = "endDate";

    public static final String QUERY_TRAINING_INSTANCE_EXT_DELETE_ALL_TRAINING_INSTANCE_EXT = "TrainingInstanceExt.deleteAllTrainingInstanceExt";
    public static final String PARAMETER_GRANT_PROGRAM_ID = "grantProgramId";


    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_tra_ins_ext_seq")
    private Long id;

    //IS - zewnętrzne id
    @OneToOne
    @JoinColumn(name = "TI_EXTERNAL_ID")
    @NotNull(message = "Należy wybrać usługodawcę")
    private TrainingInstitution trainingInstitution;

    @Column(name = "VAT_REG_NUM") //EXTERNAL_ID
    @Size(max = 20, message = "Numer NIP musi zawierać maksymalnie 20 znaków")
    private String vatRegNum;

    @Column(name = "TI_EXTERNAL_NAME") //NAME
    @NotEmpty(message = "Nazwa usługi nie może być pusta")
    @Size(max = 200, message = "Nazwa usługi musi zawierać maksymalnie 200 znaków")
    private String name;


    @OneToOne
    @JoinColumn(name = "TRAINING_ID")
    @NotNull(message = "Należy wybrać szkolenie")
    private Training training;

    @Column(name = "TRAINING_EXTERNAL_ID") // Uługa - Zewnętrzny identyfikator
    @Size(max = 30, message = "Idnetyfikator zewnętrzny usługi musi zawierać maksymalnie 30 znaków")
    private String trainingExternalId; // externalId

    @Column(name = "TRAINING_NAME") // Uługa - Nazwa
    @NotEmpty(message = "Nazwa usługi nie może być pusta")
    private String trainingName;

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

    @Column(name = "PRICE")
    @NotNull(message = "Należy podać cenę usługi")
    private BigDecimal price;

    @Column(name = "HOURS_NUMBER")
    private Integer hoursNumber;

    @Column(name = "HOUR_PRICE")
    private BigDecimal hourPrice;

    @OneToOne
    @JoinColumn(name = "TRAINING_CATEGORY_ID")
    @NotNull(message = "Należy wybrać kategorię usługi")
    private TrainingCategory category;

    @Column(name = "SUBCATEGORY_NAME")
    @Size(max = 200)
    private String subcategory;

    @Column(name = "EXAM")
    @Size(max = 20)
    private String isExam;

    @Column(name = "CERTIFICATE")
    @Size(max = 200)
    private String certificate;

    @Column(name = "CERTIFICATE_REMARK")
    @Size(max = 200, message = "Pole Certyfikat - opis musi zawierać maksymalnie 200 znaków")
    private String certificateRemark;

    @Column(name = "IND_ORDER_EXTERNAL_ID")
    @Size(max = 200, message = "Pole Identyfikator wsparcia musi zawierać maksymalnie 200 znaków")
    private String indOrderExternalId;

    @Column(name = "STATUS")
    @Size(max = 40)
    private String status;

    @Column(name = "QUALIFICATION")
    @Size(max = 200)
    private String isQualification;

    @Column(name = "OTHER_QUALIFICATION")
    @Size(max = 200)
    private String isOtherQualification;

    @Column(name = "QUALIFICATION_CODE")
    @Size(max = 200)
    private String qualificationCode;

    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @NotEmpty(message = "Maksymalna liczba uczestników nie może być pusta")
    @Column(name = "MAX_PARTICIPANTS_COUNT")
    private Integer maxParticipantsCount;

    @Column(name = "PRICE_VALIDATE_TYPE")
    @Size(max = 20)
    private String priceValidateType;

    @Column(name = "IMPORT_JOB_ID")
    private Long importJobId;

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(id, ((TrainingInstanceExt) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
