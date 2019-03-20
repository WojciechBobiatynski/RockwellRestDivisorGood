package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;

/**
 * Created by Damian.PTASZYNSKI on 2019-03-11.
 */
public interface TrainingCategoryProdInsCalcService {

    /**
     * Wylicza liczbę bonów za godzinę szkolenia do rozliczenia szkoleń
     *
     * @param productCalculateDto - dane potrzebne do wyliczenia liczby bonow
     * @return liczba bonów za godzinę dla szkolenia
     */
    Integer calculateProductInstanceForHour(ProductCalculateDto productCalculateDto);
}
