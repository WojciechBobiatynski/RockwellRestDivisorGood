package pl.sodexo.it.gryf.dao.api.search.dao;

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

}
