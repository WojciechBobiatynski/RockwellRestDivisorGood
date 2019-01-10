package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by krzysztof.krawczynsk on 2018-01-01.
 */
public class TrainingWithExternalIdSearchResultDTO extends TrainingSearchResultDTO {

    @Getter
    @Setter
    private String indOrderExternalId;

}
