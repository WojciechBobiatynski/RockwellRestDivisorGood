package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-12-27.
 */
public class TrainingInstanceUseDto {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator instancji usługi nie może być pusty")
    private Long id;

    @Getter
    @Setter
    @NotNull(message = "Pin do potwierdzenie instancji usługi nie może być pusty")
    private String pin;

    @Getter
    @Setter
    @NotNull(message = "Nowa ilosc bonów nie może być pusta")
    @Min(value = 1, message = "Nowa ilość bonów nie może być mniejesza niż 1")
    private Integer newReservationNum;

    @Getter
    @Setter
    @NotNull(message = "Wersja instancji usługi nie może być pusty")
    private Integer version;
}
