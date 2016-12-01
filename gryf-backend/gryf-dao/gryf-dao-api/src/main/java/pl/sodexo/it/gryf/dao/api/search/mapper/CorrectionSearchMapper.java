package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;

/**
 * Mapper do operacji na korektach dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface CorrectionSearchMapper {

    /**
     * Znajduje liczbę korekt dla danego rozliczenia
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    Integer findCorrectionsNumberByErmbsId(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);


}
