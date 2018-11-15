package pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions;

import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingPrecalculatedDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
public interface TrainingService {

    TrainingDTO findTraining(Long id);

    List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO training);

    List<SimpleDictionaryDto> findTrainingCategories();

    TrainingDTO createTraining();

    Long saveTraining(TrainingDTO trainingDto);

    void updateTraining(TrainingDTO trainingDto);

    boolean isTrainingInLoggedUserInstitution(Long trainingId);

    /**
     * Zwraca usługa na podstawie jego id oraz kontekstu zalogowanego użytkownika
     * @param id identyfikator usługi
     * @return dto usługi
     */
    TrainingSearchResultDTO findTrainingDetails(Long id);

    /**
     * Zwraca usługa wraz z wyliczeniami maksymalnej liczby bonów
     * na podstawie id oraz kontekstu zalogowanego użytkownika
     * @param id identyfikator usługi
     * @return dto usługi
     */
    TrainingPrecalculatedDetailsDto findTrainingPrecalculatedDetails(Long id, Long grantProgramId);

    /**
     * Zwraca listę z kategoriami usług dla programu dofinansowania
     * @param grantProgramId
     * @return
     */
    List<SimpleDictionaryDto> findTrainingCategoriesByGrantProgram(Long grantProgramId);

    /**
     * Zwraca listę ze wszystkimi katalogami usług
     * @return
     */
    List<SimpleDictionaryDto> findAllTrainingCategoryCatalogs();

    /**
     * Zwraca listę z kategoriami usług z danego katalogu
     * @param catalogId
     * @return
     */
    List<SimpleDictionaryDto> findTrainingCategoriesInCatalog(String catalogId);

    /**
     * Szukanie szkoleń przy zadanych kryteriach:
     * - Jeżeli jest ustawiony identyfikator uczestnika
     *      to wybieramy szkolenia dla danego uczestnika
     *      z zadanego prgoramu
     *
     * @param dto
     * @return Szkolenia dla danego uczestnika
     */
    List<TrainingSearchResultDTO> findTrainingsByProgramIdAndIndividualIdUsingContractsIds(TrainingSearchQueryDTO dto);
}
