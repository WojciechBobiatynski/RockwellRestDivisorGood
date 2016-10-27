package pl.sodexo.it.gryf.service.api.security.trainingInstitutions;

import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;

/**
 * Serwis do operacji na żądaniach resetu hasła dla użytkownika instytucji szkoleniowej
 *
 * Created by akmiecinski on 26.10.2016.
 */
public interface TiUserResetAttemptService {

    /**
     * Wyłącza aktywny link resetu hasła dla dane użytkownika osoby fizycznej. Maksymalnie powinien być jeden aktywny.
     * @param tiuId - id użytkownika osoby fizycznej
     */
    void disableActiveAttemptOfTiUser(Long tiuId);

    /**
     * Sprawdza czy wygenorowany link został już wygenerowany (prawie się nie zdarza) i jeśli nie to go zwraca.
     * @return nowy link
     */
    String createNewLink();

    /**
     * Zapisuje nowe żądania resetu hasła
     * @param tiUserResetAttemptDto Dto które ma być zapisane
     * @return zapisane Dto
     */
    TiUserResetAttemptDto saveTiUserResetAttempt(TiUserResetAttemptDto tiUserResetAttemptDto);

    /**
     * Sprawdza czy żądanie zmiany hasła jest wciąż aktualne
     * @param turId - id żądania
     */
    void checkIfResetAttemptStillActive(String turId);
}
