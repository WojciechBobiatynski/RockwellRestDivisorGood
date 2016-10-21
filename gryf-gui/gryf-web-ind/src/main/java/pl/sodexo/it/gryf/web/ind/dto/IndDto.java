package pl.sodexo.it.gryf.web.ind.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;
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
    private String agreementId;

    @Getter
    @Setter
    private String trainingCategory;

    @Getter
    @Setter
    private Date agreementSigningDate;

    @Getter
    @Setter
    private List<ProductDto> products;

    @Getter
    @Setter
    private List<TrainingDto> reservedTraining;

    @Getter
    @Setter
    private List<TrainingDto> settledTraining;
}