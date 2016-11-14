package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.List;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla uczestnika szkolenia.
 */
@ToString
public class IndDto extends VersionableDto {

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private List<ProductDto> products;

    @Getter
    @Setter
    private List<TrainingDto> trainings;

    @Getter
    @Setter
    private List<AgreementDto> agreements;
}