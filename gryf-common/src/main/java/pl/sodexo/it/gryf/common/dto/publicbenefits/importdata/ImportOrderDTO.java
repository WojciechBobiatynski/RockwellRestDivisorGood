package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportOrderDTO {

    @Getter
    @Setter
    @NotNull(message = "Id umowy nie może być puste")
    private Long contractId;

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
}
