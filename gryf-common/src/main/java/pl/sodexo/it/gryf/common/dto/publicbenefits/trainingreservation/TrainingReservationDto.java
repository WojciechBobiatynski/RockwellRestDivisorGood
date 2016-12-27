package pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class TrainingReservationDto {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator szkolenia nie może być pusty")
    private Long trainingId;

    @Getter
    @Setter
    @NotNull(message = "Identyfikator użytkownika nie może być pusty")
    private Long individualId;

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
    private Long contractId;

    @Getter
    @Setter
    @NotNull(message = "Ilość bonów nie może być pusta")
    private Integer toReservedNum;

    @Getter
    @Setter
    @NotNull(message = "Wersja nie może być pusta")
    private Integer version;

}
