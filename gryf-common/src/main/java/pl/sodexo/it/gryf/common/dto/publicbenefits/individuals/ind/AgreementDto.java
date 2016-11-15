package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla bonu/produktu.
 */
public class AgreementDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private List<String> trainingCategoriesNames;

    @Getter
    @Setter
    private Date signingDate;
}