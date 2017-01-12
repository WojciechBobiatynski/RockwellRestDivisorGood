package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportOrderDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Id zamówienia nie może być puste")
    @Pattern(message = "Identyfikator zamówienia musi być w formacie WKK/numer/numer", regexp = "WKK/[0-9]+/[0-9]+")
    private String externalOrderId;

    @Getter
    @Setter
    @NotNull(message = "Data zamówienia nie może być pusta")
    private Date orderDate;

    @Getter
    @Setter
    @NotNull(message = "Liczba bonów nie może być pusta")
    private Integer productInstanceNum;

    //EXTRA FIELDS

    private Long id;

    //EXTRA GETETRS

    public Long getContractId(){
        if(id == null) {
            String[] tab = externalOrderId.split("/");
            id = Long.valueOf(tab[1]);
        }
        return id;
    }
}
