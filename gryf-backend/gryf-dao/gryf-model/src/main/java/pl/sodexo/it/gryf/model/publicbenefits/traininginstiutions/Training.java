package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

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
import java.util.Objects;

/**
 * Encja reprezentuająca szkolenie
 *
 * Created by kantczak on 2016-10-26.
 */
@Entity
@Table(name = "TI_TRAININGS", schema = "APP_PBE")
@SequenceGenerator(name = "ti_tra_seq", schema = "eagle", sequenceName = "ti_tra_seq", allocationSize = 1)
@ToString
public class Training extends VersionableEntity {

    public static final String END_DATE_ATTR_NAME = "endDate";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_tra_seq")
    private Long id;

    @OneToOne
    @JoinColumn(name = "TRAINING_INSTITUTION_ID")
    @NotNull(message = "Należy wybrać instytucję szkoleniową")
    private TrainingInstitution trainingInstitution;

    @Column(name = "NAME")
    @NotEmpty(message = "Nazwa szkolenia nie może być pusta")
    private String name;

    @Column(name = "PRICE")
    @NotNull(message = "Należy podać cenę szkolenia")
    private BigDecimal price;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Należy podać datę rozpoczęcia szkolenia")
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Należy podać datę zakończenia szkolenia")
    private Date endDate;

    @Column(name = "PLACE")
    @NotEmpty(message = "Należy podać miejsce szkolenia")
    private String place;

    @Column(name = "HOURS_NUMBER")
    @NotNull(message = "Należy podać liczbę godzin szkolenia")
    private Integer hoursNumber;

    @Column(name = "HOUR_PRICE")
    @NotNull(message = "Należy podać cenę za godzinę szkolenia")
    private BigDecimal hourPrice;

    @OneToOne
    @JoinColumn(name = "TRAINING_CATEGORY_ID")
    @NotNull(message = "Należy wybrać kategorię szkolenia")
    private TrainingCategory category;

    @Column(name = "REIMBURSMENT_CONDITIONS")
    private String reimbursmentConditions;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingInstitution getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(TrainingInstitution trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getHoursNumber() {
        return hoursNumber;
    }

    public void setHoursNumber(Integer hoursNumber) {
        this.hoursNumber = hoursNumber;
    }

    public BigDecimal getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(BigDecimal hourPrice) {
        this.hourPrice = hourPrice;
    }

    public TrainingCategory getCategory() {
        return category;
    }

    public void setCategory(TrainingCategory category) {
        this.category = category;
    }

    public String getReimbursmentConditions() {
        return reimbursmentConditions;
    }

    public void setReimbursmentConditions(String reimbursmentConditions) {
        this.reimbursmentConditions = reimbursmentConditions;
    }

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
