package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;

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

}
