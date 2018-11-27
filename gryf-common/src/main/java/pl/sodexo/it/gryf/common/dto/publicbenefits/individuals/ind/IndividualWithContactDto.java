package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla uczestnika usługi.
 */
@ToString
public class IndividualWithContactDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

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
    private String verificationEmail;
}