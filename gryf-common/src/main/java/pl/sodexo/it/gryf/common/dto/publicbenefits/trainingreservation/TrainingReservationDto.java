package pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TrainingReservationDto {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator usługi nie może być pusty")
    private Long trainingId;

    @Getter
    @Setter
    @NotNull(message = "Identyfikator użytkownika nie może być pusty")
    private Long individualId;

    @Getter
    @Setter
    @NotEmpty(message = "Kod weryfikacyjny nie może być pusty")
    private String verificationCode;

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
    private String contractId;

    @Getter
    @Setter
    @NotNull(message = "Ilość bonów nie może być pusta")
    private Integer toReservedNum;

    @Getter
    @Setter
    @NotNull(message = "Wersja nie może być pusta")
    private Integer version;

    @Getter
    @Setter
    @NotNull(message = "Nazwa programu nie może być pusta")
    private String grantProgramName;

    @Getter
    @Setter
    @NotNull(message = "Identyfikator programu nie może być pusty")
    private Long grantProgramId;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;
}
