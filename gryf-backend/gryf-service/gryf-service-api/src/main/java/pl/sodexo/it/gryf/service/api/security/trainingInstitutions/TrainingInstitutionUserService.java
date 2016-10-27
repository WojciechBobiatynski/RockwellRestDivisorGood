package pl.sodexo.it.gryf.service.api.security.trainingInstitutions;

import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;

/**
 * Klasa zajmująca się operacjami związanymi z użytkownikiem instytucji szkoleniowej
 *
 * Created by akmiecinski on 24.10.2016.
 */
public interface TrainingInstitutionUserService {

    /**
     * Zapis użytkownika instytucji szkoleniowej
     * @param gryfTiUserDto - dto użytkownika instytucji szkoleniowej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika instytucji szkoleniowej
     */
    GryfTiUserDto saveTiUser(GryfTiUserDto gryfTiUserDto);

    /**
     * Zapis i flush użytkownika instytucji szkoleniowej
     * @param gryfTiUserDto - dto użytkownika instytucji szkoleniowej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika instytucji szkoleniowej
     */
    GryfTiUserDto saveAndFlushTiUser(GryfTiUserDto gryfTiUserDto);

    /**
     * Zapis i flush użytkownika instytucji szkoleniowej w nowej transakcji
     * @param gryfTiUserDto - dto użytkownika instytucji szkoleniowej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika instytucji szkoleniowej
     */
    GryfTiUserDto saveAndFlushTiUserInNewTransaction(GryfTiUserDto gryfTiUserDto);

    /**
     * Wyszukuje użytkownika instytucji szkoleniowej dla zadanego loginu
     * @param login - login użytkownika
     * @return Dto użytkownika instytucji szkoleniowej
     */
    GryfTiUserDto findTiUserByLogin(String login);

    /**
     * Wyszukuje użytkownika instytucji szkoleniowej dla zadanego loginu
     * @param email
     * @return
     */
    GryfTiUserDto findTiUserByEmail(String email);

    /**
     * Znajduje użytkownika instytucji szkoleniowej i zapisuje dla niego nowe hasło
     * @param turId - id żądania zmiany hasła
     * @param password - nowe hasło
     * @return dto użytkownika instytucji szkoleniowej
     */
    GryfTiUserDto findUserByTurIdAndSaveNewPassword(String turId, String password);
}
