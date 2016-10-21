package pl.sodexo.it.gryf.service.api.security.individuals;

import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;

/**
 * Klasa zajmująca się operacjami związanymi z użytkownikiem osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
public interface IndividualUserService {

    /**
     * Zapis usera osoby fizycznej
     * @param gryfIndUserDto - dto użytkownika osoby fizycznej, którą chcemy zapisać
     * @return zaktualizowane Dto użytkownika osoby fizycznej
     */
    GryfIndUserDto saveIndUser(GryfIndUserDto gryfIndUserDto);

}
