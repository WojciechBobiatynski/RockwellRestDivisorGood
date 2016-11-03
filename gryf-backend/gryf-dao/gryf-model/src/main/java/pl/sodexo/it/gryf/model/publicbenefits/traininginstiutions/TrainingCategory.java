package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.SimpleDictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Encja reprezentuająca kategorię szkolenia
 *
 * Created by kantczak on 2016-10-26.
 */
@Entity
@Table(name = "TI_TRAINING_CATEGORY_DICS", schema = "APP_PBE")
@ToString
public class TrainingCategory extends SimpleDictionaryEntity {

    @Id
    @Column(name = "TRC_CODE")
    @Getter
    @Setter
    private String code;
}
