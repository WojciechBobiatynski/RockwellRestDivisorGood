package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.generator.IdentityGenerator;
import pl.sodexo.it.gryf.common.validation.publicbenefits.ValidExternalOrderId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
@ValidExternalOrderId (message = "Identyfikator umowy musi być w formacie kod programu/numer/numer")
public class ImportContractDTO {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
    private String externalOrderId;

    @Getter
    @Setter
    @NotEmpty(message = "Rodzaj umowy nie może być pusty")
    private String contractType;

    @Getter
    @Setter
    @NotNull(message = "Data podpisania umowy nie może być pusta")
    private Date signDate;

    @Getter
    @Setter
    @NotNull(message = "Liczba bonów nie może być pusta")
    private Integer productInstanceNum;

    @Getter
    @Setter
    @NotNull(message = "Data ważności umowy nie może być pusta")
    private Date expiryDate;

    @Getter
    @Setter
    @Size(message = "Kategorie usługi przydzielone uczestnikowi nie mogą być puste", min = 1)
    private List<String> contractTrainingCategories;

    @Getter
    @Setter
    @NotNull(message = "Programu nie może być pusty")
    private GrantProgramDictionaryDTO grantProgram;


    //EXTRA GETETRS
    public String getId( IdentityGenerator<ImportContractDTO, String> identityGenerator){
        return identityGenerator.generate(this);
    }

}

