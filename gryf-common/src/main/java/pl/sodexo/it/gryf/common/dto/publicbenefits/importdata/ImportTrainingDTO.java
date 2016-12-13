package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportTrainingDTO {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator IS nie może być pusty")
    private String trainingInstitutionExternalId;

    @Getter
    @Setter
    @NotEmpty(message = "NIP nie może być pusty")
    private String vatRegNum;

    @Getter
    @Setter
    @NotEmpty(message = "Nazwa IS nie może być pusta")
    private String trainingInstanceName;

    @Getter
    @Setter
    @NotEmpty(message = "Identyfikator szkolenia nie może być pusty")
    private String externalId;

    @Getter
    @Setter
    @NotEmpty(message = "Nazwa szkolenia nie może być pusta")
    private String name;

    @Getter
    @Setter
    @NotNull(message = "Data rozpoczęcia nie może być pusta")
    private Date startDate;

    @Getter
    @Setter
    @NotNull(message = "Data zakończenia nie może być pusta")
    private Date endDate;

    @Getter
    @Setter
    @NotEmpty(message = "Miejsce szkolenia nie może być puste")
    private String place;

    @Getter
    @Setter
    @NotNull(message = "Cena szkolenia nie może być pusta")
    private BigDecimal price;

    @Getter
    @Setter
    @NotNull(message = "Ilość godzin lekcyjnych nie może być pusta")
    private Integer hoursNumber;

    @Getter
    @Setter
    @NotNull(message = "Cena 1h szkolenia nie może być pusta")
    private BigDecimal hourPrice;

    @Getter
    @Setter
    @NotEmpty(message = "Kategorie szkolenia nie może być pusta")
    private String category;

    @Getter
    @Setter
    @NotEmpty(message = "Warunki rozliczenia szkolenia nie mogą być puste")
    private String reimbursmentCondition;
}
