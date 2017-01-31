package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportContractDTO {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
    @Pattern(message = "Identyfikator umowy musi być w formacie WKK/numer/numer", regexp = "WKK/[0-9]+/[0-9]+")
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

    //EXTRA FIELDS

    private Long id;


    //EXTRA GETETRS

    public Long getId(){
        if(id == null) {
            String[] tab = externalOrderId.split("/");
            id = Long.valueOf(tab[1]);
        }
        return id;
    }

}
