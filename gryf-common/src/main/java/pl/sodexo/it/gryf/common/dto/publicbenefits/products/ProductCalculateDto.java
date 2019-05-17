package pl.sodexo.it.gryf.common.dto.publicbenefits.products;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Dane potrzebne do wyliczenia liczby bonow za jedna godzine dla szkolenia
 *
 * Created by Damian.PTASZYNSKI on 2019-03-19.
 */
@Getter
@Setter
public class ProductCalculateDto {

    private Long trainingId;

    private String categoryId;

    private Long grantProgramId;

    private Date date;

    private boolean isIndividualTraining;


}
