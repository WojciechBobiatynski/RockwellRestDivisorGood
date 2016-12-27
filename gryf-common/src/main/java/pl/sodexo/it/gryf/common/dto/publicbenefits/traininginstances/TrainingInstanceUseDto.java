package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Isolution on 2016-12-27.
 */
public class TrainingInstanceUseDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String pin;

    @Getter
    @Setter
    private Integer version;
}
