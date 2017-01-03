package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by Isolution on 2017-01-03.
 */
public class ImportOpinionDoneDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Identyfikator szkolenia nie może być pusty")
    private String trainingExternalId;

    @Getter
    @Setter
    @NotEmpty(message = "PESEL nie może być pusty")
    private String pesel;

    @Getter
    @Setter
    @NotEmpty(message = "Status oceny nie może być pusty")
    @Pattern(message = "Status oceny może przyjmować tylko wartości 'T' lub 'N'", regexp = "[TN]")
    private String opinionDone;
}
