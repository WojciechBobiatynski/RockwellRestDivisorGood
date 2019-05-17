package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
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

    @Column(name = "INDIVIDUAL_PRODUCT_INSTANCE")
    private Integer individualProductInstance;

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

    @ManyToOne
    @JoinColumn(name = "PRODUCT_INSTANCE_CALC_TYPE", referencedColumnName = "ID")
    private TrainingCategoryProdInsCalcType productInstanceCalcType;

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
