package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.generator.IdentityGenerator;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
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

    @Getter
    @Setter
    private BigDecimal ownContributionPercentage;

    //EXTRA GETETRS
    public String getContractId(IdentityGenerator<ImportOrderDTO, String> identityGenerator){
        return identityGenerator.generate(this);
    }
}
