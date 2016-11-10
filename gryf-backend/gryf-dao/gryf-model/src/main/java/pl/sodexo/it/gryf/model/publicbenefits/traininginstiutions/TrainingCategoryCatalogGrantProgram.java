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
@ToString(exclude = {"grantProgram", "catalog"})
@Table(name = "TI_TRAINING_CATEGORY_CAT_GR_PR", schema = "APP_PBE")
public class TrainingCategoryCatalogGrantProgram extends GryfEntity{

    //FIELDS

    @Id
    private Long id;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", insertable=false, updatable=false)
    private GrantProgram grantProgram;

    @ManyToOne
    @JoinColumn(name = "CATALOG_ID", insertable=false, updatable=false)
    private TrainingCategoryCatalog catalog;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public TrainingCategoryCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(TrainingCategoryCatalog catalog) {
        this.catalog = catalog;
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
        return Objects.equals(id, ((TrainingCategoryCatalogGrantProgram) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
