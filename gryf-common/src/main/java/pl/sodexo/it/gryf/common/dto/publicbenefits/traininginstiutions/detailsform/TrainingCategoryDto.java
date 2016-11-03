package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;

@ToString(callSuper = true)
public class TrainingCategoryDto extends SimpleDictionaryDto {

    @Getter
    @Setter
    private String code;
}
