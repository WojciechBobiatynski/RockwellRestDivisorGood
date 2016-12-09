package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;

import java.util.List;

/**
 * Serwis realizujący operacje na załącznikach do korekt
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface CorrectionAttachmentService {

    /**
     * Pobiera plik załącznik na podstawie ID
     * @param id - id załącznika
     * @return - dto załącznika
     */
    FileDTO getCorrAttFileById(Long id);

    /**
     * Tworzy załączniki dla korekt, gdy jeszcze nie istnieją
     * @param elctRmbsHeadDto - dto rozliczenia
     * @return - dto dla załączników korkety
     */
    List<CorrectionAttachmentDto> createCorrAttIfNotExistsForErmbsAttBeingChanged(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     *
     * @param elctRmbsHeadDto - dto rozliczenia
     * @param ermbsAttachmentDto - dto załącznika
     * @return true/false
     */
    CorrectionAttachmentDto getCorrAttByAttByErmbsAttIdAndCorrId(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto);

    /**
     * Zapisuje listę załączników do korekt
     * @param correctionAttachments - lista załączników
     */
    void saveCorrectionAttachments(List<CorrectionAttachmentDto> correctionAttachments);

}
