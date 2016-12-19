package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionNotificationEmailParamsDto;

import java.util.List;

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

    /**
     * Znajduje wszystkie korekty wskazanego rozliczenia wraz z ich numerami
     * @param ermbsId - id rozliczenia
     * @return lista wszystkich korekt rozliczenia
     */
    List<CorrectionDto> findCorrectionsByERmbsId(Long ermbsId);

    /**
     * Znajduje parametry potrzebne do wysyłki maila
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return dto z parametrami
     */
    CorrectionNotificationEmailParamsDto findCorrNotifParamsByErmbsId(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);
}
