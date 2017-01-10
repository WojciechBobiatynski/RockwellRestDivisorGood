package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class TrainingInstanceDataToValidateDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private boolean opinionDone;

    @Getter
    @Setter
    private Long trainingInstitutionId;
}
