package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.dto.security.VerificationDto;

/**
 * Mapper do wyszukiwania wyników związanych z osobą fizyczną
 *
 * Created by akmiecinski on 18.10.2016.
 */
public interface IndividualSearchMapper {

    /**
     * Wyszukuje ID osoby fizycznej na podstawie numeru pesel i adresu email
     * @param verificationDto - obiekt z peselem i adresem email
     * @return - ID osoby fizycznej
     */
    Long findIndividualIdByPeselAndEmail(@Param("criteria")VerificationDto verificationDto);

}
