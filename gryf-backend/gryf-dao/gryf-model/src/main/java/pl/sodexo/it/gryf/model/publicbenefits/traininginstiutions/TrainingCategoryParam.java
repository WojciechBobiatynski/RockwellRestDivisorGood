package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-10.
 */
@Entity
@ToString(exclude = {"grantProgram", "category"})
@Table(name = "TI_TRAINING_CATEGORY_PARAMS", schema = "APP_PBE")
@NamedQueries(
        {@NamedQuery(name = "TrainingCategoryParam.findByCategoryAndGrantProgramInDate", query="select distinct tccp from TrainingCategoryParam tccp " +
                "where tccp.category.id = :categoryId " +
                "and  tccp.grantProgram.id = :grantProgramId " +
                "and (tccp.dateFrom is null or tccp.dateFrom <= :date) " +
                "and (tccp.dateTo is null or :date <= tccp.dateTo)")})
public class TrainingCategoryParam extends GryfEntity {

    //FIELDS

    @Id
    private Long id;

    @Column(name = "PRODUCT_INSTANCE_FOR_HOUR")
    private Integer productInstanceForHour;

    @Column(name = "MAX_PRODUCT_INSTANCE")
    private Integer maxProductInstance;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", insertable=false, updatable=false)
    private TrainingCategory category;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", insertable=false, updatable=false)
    private GrantProgram grantProgram;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductInstanceForHour() {
        return productInstanceForHour;
    }

    public void setProductInstanceForHour(Integer productInstanceForHour) {
        this.productInstanceForHour = productInstanceForHour;
    }

    public Integer getMaxProductInstance() {
        return maxProductInstance;
    }

    public void setMaxProductInstance(Integer maxProductInstance) {
        this.maxProductInstance = maxProductInstance;
    }

    public TrainingCategory getCategory() {
        return category;
    }

    public void setCategory(TrainingCategory category) {
        this.category = category;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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
        return Objects.equals(id, ((TrainingCategoryParam) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
