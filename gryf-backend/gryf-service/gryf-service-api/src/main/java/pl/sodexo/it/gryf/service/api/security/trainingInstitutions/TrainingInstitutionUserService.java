package pl.sodexo.it.gryf.service.api.security.trainingInstitutions;

import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;

/**
 * Klasa zajmująca się operacjami związanymi z użytkownikiem Usługodawcy
 *
 * Created by akmiecinski on 24.10.2016.
 */
public interface TrainingInstitutionUserService {

    /**
     * Zapis użytkownika Usługodawcy
     * @param gryfTiUserDto - dto użytkownika Usługodawcy, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika Usługodawcy
     */
    GryfTiUserDto saveTiUser(GryfTiUserDto gryfTiUserDto);

    /**
     * Zapis i flush użytkownika Usługodawcy
     * @param gryfTiUserDto - dto użytkownika Usługodawcy, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika Usługodawcy
     */
    GryfTiUserDto saveAndFlushTiUser(GryfTiUserDto gryfTiUserDto);

    /**
     * Zapis i flush użytkownika Usługodawcy w nowej transakcji
     * @param gryfTiUserDto - dto użytkownika Usługodawcy, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika Usługodawcy
     */
    GryfTiUserDto saveAndFlushTiUserInNewTransaction(GryfTiUserDto gryfTiUserDto);

    /**
     * Wyszukuje użytkownika Usługodawcy dla zadanego loginu
     * @param login - login użytkownika
     * @return Dto użytkownika Usługodawcy
     */
    GryfTiUserDto findTiUserByLogin(String login);

    /**
     * Wyszukuje użytkownika Usługodawcy dla zadanego loginu
     * @param email
     * @return
     */
    GryfTiUserDto findTiUserByEmail(String email);

    /**
     * Znajduje użytkownika Usługodawcy i zapisuje dla niego nowe hasło
     * @param turId - id żądania zmiany hasła
     * @param password - nowe hasło
     * @return dto użytkownika Usługodawcy
     */
    GryfTiUserDto findUserByTurIdAndSaveNewPassword(String turId, String password);
}
