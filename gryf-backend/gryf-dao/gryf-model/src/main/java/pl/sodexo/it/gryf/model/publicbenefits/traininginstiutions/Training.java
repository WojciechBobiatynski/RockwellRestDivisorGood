package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Encja reprezentuająca szkolenie
 *
 * Created by kantczak on 2016-10-26.
 */
@Entity
@Table(name = "TI_TRAININGS", schema = "APP_PBE")
@SequenceGenerator(name = "ti_tra_seq", schema = "eagle", sequenceName = "ti_tra_seq", allocationSize = 1)
@ToString
public class Training extends AuditableEntity {

    public static final String END_DATE_ATTR_NAME = "endDate";

    @Id
    @Column(name = "TRA_ID")
    @GeneratedValue(generator = "ti_tra_seq")
    @Getter
    @Setter
    private Long traId;

    @OneToOne
    @JoinColumn(name = "TRIN_ID")
    @Getter
    @Setter
    @NotNull(message = "Należy wybrać instytucję szkoleniową")
    private TrainingInstitution trainingInstitution;

    @Column(name = "NAME")
    @Getter
    @Setter
    @NotEmpty(message = "Nazwa szkolenia nie może być pusta")
    private String name;

    @Column(name = "PRICE")
    @Getter
    @Setter
    @NotNull(message = "Należy podać cenę szkolenia")
    private BigDecimal price;

    @Getter
    @Setter
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Należy podać datę rozpoczęcia szkolenia")
    private Date startDate;

    @Getter
    @Setter
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Należy podać datę zakończenia szkolenia")
    private Date endDate;

    @Column(name = "PLACE")
    @Getter
    @Setter
    @NotEmpty(message = "Należy podać miejsce szkolenia")
    private String place;

    @Column(name = "HOURS_NUMBER")
    @Getter
    @Setter
    @NotNull(message = "Należy podać liczbę godzin szkolenia")
    private BigDecimal hoursNumber;

    @Column(name = "HOUR_PRICE")
    @Getter
    @Setter
    @NotNull(message = "Należy podać cenę za godzinę szkolenia")
    private BigDecimal hourPrice;

    @OneToOne
    @JoinColumn(name = "TRC_CODE")
    @Getter
    @Setter
    @NotNull(message = "Należy wybrać kategorię szkolenia")
    private TrainingCategory category;
}
