package pl.sodexo.it.gryf.common.dto.publicbenefits.products;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Dane potrzebne do wyliczenia liczby bonow za jedna godzine dla szkolenia
 *
 * Created by Damian.PTASZYNSKI on 2019-03-19.
 */
public class ProductCalculateDto {

    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private String categoryId;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private Date date;


}
