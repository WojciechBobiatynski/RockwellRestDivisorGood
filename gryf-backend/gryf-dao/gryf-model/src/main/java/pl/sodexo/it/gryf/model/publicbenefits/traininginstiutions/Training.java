package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.BooleanConverter;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;

import javax.persistence.*;
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
@NamedQueries({
        @NamedQuery(name = "Training.findByExternalId", query = "select e from Training e where e.externalId = :externalId "),
        @NamedQuery(name = "Training.deactiveTrainings", query = "update Training tt set tt.active = false, tt.deactivateDate = CURRENT_TIMESTAMP, "
                + "tt.deactivateJob = :importJob, tt.version = (tt.version + 1), tt.modifiedTimestamp = CURRENT_TIMESTAMP, tt.modifiedUser = :modifiedUser "
                + "where tt.active = true and tt.id not in "
                + "(select r.training.id from ImportDataRow r where  r.training.id is not null and r.importJob = :importJob)")})
@ToString
public class Training extends VersionableEntity {

    public static final String END_DATE_ATTR_NAME = "endDate";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_tra_seq")
    private Long id;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

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

    @Convert(converter = BooleanConverter.class)
    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "DEACTIVATE_USER")
    private String deactivateUser;

    @Column(name = "DEACTIVATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deactivateDate;

    @ManyToOne
    @JoinColumn(name = "DEACTIVATE_JOB_ID")
    private AsynchronizeJob deactivateJob;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDeactivateUser() {
        return deactivateUser;
    }

    public void setDeactivateUser(String deactivateUser) {
        this.deactivateUser = deactivateUser;
    }

    public Date getDeactivateDate() {
        return deactivateDate;
    }

    public void setDeactivateDate(Date deactivateDate) {
        this.deactivateDate = deactivateDate;
    }

    public AsynchronizeJob getDeactivateJob() {
        return deactivateJob;
    }

    public void setDeactivateJob(AsynchronizeJob deactivateJob) {
        this.deactivateJob = deactivateJob;
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
