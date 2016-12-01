package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;

import java.util.List;

/**
 * Dao dla operacji korektach dla rozliczeń
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface CorrectionSearchDao {

    /**
     * Znajduje liczbę korekt dla danego rozliczenia
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    Integer findCorrectionsNumberByErmbsId(Long ermbsId);

    /**
     * Znajduje wszystkie korekty wskazanego rozliczenia wraz z ich numerami
     * @param ermbsId - id rozliczenia
     * @return lista wszystkich korekt rozliczenia
     */
    List<CorrectionDto> findCorrectionsByERmbsId(Long ermbsId);
}
