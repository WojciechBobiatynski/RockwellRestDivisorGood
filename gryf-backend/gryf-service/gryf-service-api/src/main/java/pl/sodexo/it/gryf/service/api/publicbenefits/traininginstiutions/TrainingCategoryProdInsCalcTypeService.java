package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;


/**
 * Created by Damian.PTASZYNSKI on 2019-03-07.
 */
public interface TrainingCategoryProdInsCalcTypeService {

    /**
     * Wylicza liczbę bonów za godzinę szkolenia do rozliczenia szkoleń
     *
     * @param trainingReservationDto - rezerwacja szkolenia
     * @return liczba bonów za godzinę dla szkolenia
     */
    Integer getCalculateProductInstanceForHour(TrainingReservationDto trainingReservationDto);
}
