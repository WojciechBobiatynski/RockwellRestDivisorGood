package pl.sodexo.it.gryf.service.api.security.individuals;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfIndUser;

/**
 * Klasa zajmująca się operacjami związanymi z użytkownikiem osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
public interface IndividualUserService {

    /**
     * Zajudje użytkownika osoby fizycznej na podstawie numeru pesel
     * @param pesel - pesel użytkownika
     * @return Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto findByPesel(String pesel);

    /**
     * Zajudje użytkownika osoby fizycznej na podstawie numeru pesel i adresu email
     * @param pesel - pesel użytkownika
     * @param email - email użytkownika
     * @return Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto findByPeselAndEmail(String pesel, String email);

    /**
     * Zapis usera osoby fizycznej
     * @param gryfIndUserDto - dto użytkownika osoby fizycznej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto saveIndUser(GryfIndUserDto gryfIndUserDto);

    /**
     * Zapis i flush użytkownika osoby fizycznej
     * @param gryfIndUserDto - dto użytkownika osoby fizycznej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto saveAndFlushIndUser(GryfIndUserDto gryfIndUserDto);

    /**
     * Zapis i flush użytkownika osoby fizycznej w nowej transakcji
     * @param gryfIndUserDto - dto użytkownika osoby fizycznej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto saveAndFlushIndUserInNewTransaction(GryfIndUserDto gryfIndUserDto);

    /**
     * Tworzy i zapisuje nowego użytkownika osoby fizycznej na podstawie Dto osoby fizycznej
     * @param individualDto - dto osoby fizycznej
     * @param verificationCode - dto osoby fizycznej
     * @return Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto createAndSaveNewUser(IndividualDto individualDto, String verificationCode);

    /**
     * Ustawia datę ostatniego logowania i liczbę niepoprawnych prób logowania dla uzytkownika osoby fizycznej
     *
     * @param gryfIndUser - zalogowany uzytkownik
     */
    void updateIndAfterSuccessLogin (GryfIndUser gryfIndUser);

    /**
     * Zajudje użytkownika osoby fizycznej na id osoby fizycznej
     * @param individualId - id osoby fizycznej
     * @param verificationCode - nowy kod weryfikacyjny
     * @return Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto saveNewVerificationCodeForIndividual(Long individualId, String verificationCode);

}
