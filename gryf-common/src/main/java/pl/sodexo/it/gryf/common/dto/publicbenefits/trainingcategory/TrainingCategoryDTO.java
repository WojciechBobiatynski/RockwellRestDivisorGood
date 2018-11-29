package pl.sodexo.it.gryf.common.dto.publicbenefits.trainingcategory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.GryfDto;

@ToString
public class TrainingCategoryDTO extends GryfDto {

    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Integer ordinal;
}

