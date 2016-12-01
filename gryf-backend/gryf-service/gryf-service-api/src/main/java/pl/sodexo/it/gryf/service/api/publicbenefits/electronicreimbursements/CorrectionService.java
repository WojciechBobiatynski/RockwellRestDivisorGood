package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;

import java.util.Date;
import java.util.List;

/**
 * Serwis realizujący operacje na korektach
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface CorrectionService {

    /**
     * Metoda tworząca i zapisująca nową korektę dla podanego Id rozliczenia
     * @param correctionDto - dto korekty
     * @return - id nowej korekty
     */
    Long createAndSaveCorrection(CorrectionDto correctionDto);

    /**
     * Metoda służąca do wyliczania wymaganej daty udzielenia korekty
     * @return wymagana data udzielenia korekty
     */
    Date getRequiredCorrectionDate();

    /**
     * Znajduje wszystkie korekty wskazanego rozliczenia wraz z ich numerami
     * @param ermbsId - id rozliczenia
     * @return lista wszystkich korekt rozliczenia
     */
    List<CorrectionDto> findCorrectionsByERmbsId(Long ermbsId);
    /**
     * Znajduje liczbę korekt dla danego rozliczenia
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    Integer findCorrectionsNumberByErmbsId(Long ermbsId);

    /**
     * Uzupełnia dane korekty przy wysyłce do
     * @param correctionId
     * @return
     */
    Long completeCorrection(Long correctionId);

}
