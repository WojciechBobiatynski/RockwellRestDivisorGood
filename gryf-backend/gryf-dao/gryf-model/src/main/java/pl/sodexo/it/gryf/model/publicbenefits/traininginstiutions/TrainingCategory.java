package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory.QUERY_FIND_BY_GRANT_PROGRAM;
import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory.QUERY_FIND_BY_GRANT_PROGRAM_AND_NAME;

/**
 * Encja reprezentuająca kategorię usługi
 *
 * Created by kantczak on 2016-10-26.
 */
@Entity
@Table(name = "TI_TRAINING_CATEGORIES", schema = "APP_PBE")
@ToString
@NamedQueries({@NamedQuery(name = QUERY_FIND_BY_GRANT_PROGRAM, query = "select distinct tc from TrainingCategoryCatalogGrantProgram tccgp " +
        "join tccgp.catalog c join c.trainingCategories tc " +
        "where tccgp.grantProgram.id = :grantProgramId "),
        @NamedQuery(name = "TrainingCategory.findByCatalogId", query = "select tcc.trainingCategories from TrainingCategoryCatalog tcc " +
        "where tcc.id = :catalogId "),
        @NamedQuery(name = "TrainingCategory.findByIdList", query = "select tc from TrainingCategory tc " +
                "where tc.id in :idList "),
        @NamedQuery(name = QUERY_FIND_BY_GRANT_PROGRAM_AND_NAME, query = "select distinct tc from TrainingCategoryCatalogGrantProgram tccgp " +
                "join tccgp.catalog c join c.trainingCategories tc " +
                "where tccgp.grantProgram.id = :grantProgramId " +
                " and tc.name = :name")
})
public class TrainingCategory extends GryfEntity implements DictionaryEntity {

    public static final String QUERY_FIND_BY_GRANT_PROGRAM = "TrainingCategory.findByGrantProgram";
    public static final String QUERY_FIND_BY_GRANT_PROGRAM_AND_NAME = "QUERY_FIND_BY_GRANT_PROGRAM_AND_NAME";
    public static final String PARAMETER_NAME = "name";

    public static final String EGZ_TYPE = "EGZ";

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private TrainingCategory parent;

    @JsonManagedReference("trainingCategoryCatalog")
    @JoinColumn(name = "CATALOG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TrainingCategoryCatalog trainingCategoryCatalog;

    //DICTIONARY METHODS

    public Object getDictionaryId() {
        return id;
    }

    public String getDictionaryName() {
        return name;
    }

    public String getOrderField(){
        return "name";
    }

    public String getActiveField() {
        return null;
    }

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public TrainingCategory getParent() {
        return parent;
    }

    public void setParent(TrainingCategory parent) {
        this.parent = parent;
    }

    public TrainingCategoryCatalog getTrainingCategoryCatalog() {
        return trainingCategoryCatalog;
    }

    public void setTrainingCategoryCatalog(TrainingCategoryCatalog trainingCategoryCatalog) {
        this.trainingCategoryCatalog = trainingCategoryCatalog;
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
        return Objects.equals(id, ((TrainingCategory) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
