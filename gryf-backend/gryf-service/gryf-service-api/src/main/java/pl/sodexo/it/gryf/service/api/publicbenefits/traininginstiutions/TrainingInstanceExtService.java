package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
public interface TrainingInstanceExtService {


    TrainingInstanceExtDTO createTrainingInstanceExt();

    /**
     * Zapisuje zawartość pliku ze szkoleniami od BUR
     *
     * @param trainingInstanceExtDto - wiersz usługi
     * @param importJobId            - Id importu
     * @return id usługi
     */
    Long saveTrainingInstanceExt(TrainingInstanceExtDTO trainingInstanceExtDto, Long importJobId);


}
