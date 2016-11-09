package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;

import java.util.List;

/**
 * Serwis realizujacy uslugi dotyczące uwierzytelniania uzytkownika po stronie aplikacji
 *
 * Created by akmiecinski on 21.10.2016.
 */
public interface SecurityService {

    /**
     * Metoda wyszukująca użytkownika osoby fizycznej po peselu
     * @param pesel - pesel osoby fizycznej
     * @return Dto reprezentujące użytkonwika osoby fizycznej na potrzeby autentykacji
     */
    GryfIndUserDto findIndUserByPesel(String pesel);

    /**
     * Metoda wyszukująca role dla instytucji szkoleniowej
     * @return lista ról
     */
    List<RoleDto> findRolesForTiUser();

}
