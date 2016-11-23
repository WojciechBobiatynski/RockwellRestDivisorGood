package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-10.
 */
@Entity
@ToString
@Table(name = "TI_TRAINING_CATEGORY_CAT", schema = "APP_PBE")
public class TrainingCategoryCatalog extends GryfEntity implements DictionaryEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    @JsonBackReference("trainingCategoryCatalog")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingCategoryCatalog")
    private List<TrainingCategory> trainingCategories;

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

    public List<TrainingCategory> getTrainingCategories() {
        return trainingCategories;
    }

    public void setTrainingCategories(List<TrainingCategory> trainingCategories) {
        this.trainingCategories = trainingCategories;
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
        return Objects.equals(id, ((TrainingCategoryCatalog) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
