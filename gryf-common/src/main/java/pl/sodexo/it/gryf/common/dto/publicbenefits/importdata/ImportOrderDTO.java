package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.generator.IdentityGenerator;
import pl.sodexo.it.gryf.common.validation.publicbenefits.ValidExternalOrderId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
@ValidExternalOrderId(message = "Identyfikator umowy musi być w formacie kod programu/numer/numer")
public class ImportOrderDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Id zamówienia nie może być puste")
    private String externalOrderId;

    @Getter
    @Setter
    @NotNull(message = "Data zamówienia nie może być pusta")
    private Date orderDate;

    @Getter
    @Setter
    @NotNull(message = "Liczba bonów nie może być pusta")
    private Integer productInstanceNum;

    //EXTRA GETETRS
    public String getContractId(IdentityGenerator<ImportOrderDTO, String> identityGenerator){
        return identityGenerator.generate(this);
    }
}
