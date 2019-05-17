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
@Getter
@Setter
public class ImportTrainingDTO {

    @NotNull(message = "Identyfikator IS nie może być pusty")
    private String trainingInstitutionExternalId;

    @NotEmpty(message = "NIP nie może być pusty")
    private String vatRegNum;

    @NotEmpty(message = "Nazwa IS nie może być pusta")
    private String trainingInstanceName;

    @NotEmpty(message = "Identyfikator usługi nie może być pusty")
    private String externalId;

    @NotEmpty(message = "Nazwa usługi nie może być pusta")
    private String name;

    @NotNull(message = "Data rozpoczęcia nie może być pusta")
    private Date startDate;

    @NotNull(message = "Data zakończenia nie może być pusta")
    private Date endDate;

    @NotEmpty(message = "Miejsce usługi nie może być puste")
    private String place;

    @NotNull(message = "Cena usługi nie może być pusta")
    private BigDecimal price;

    private Integer hoursNumber;

    private BigDecimal hourPrice;

    @NotEmpty(message = "Kategorie usługi nie może być pusta")
    private String category;

    private String subcategory;

    private String isExam;

    private String certificate;

    private String certificateRemark;

    private String indOrderExternalId;

    private String status;

    private String isQualification;

    private String isOtherQualification;

    private String qualificationCode;

    private Date registrationDate;

    @NotNull(message = "Maksymalna liczba uczestników nie może być pusta")
    private Integer maxParticipantsCount;

    private String priceValidateType;

}
