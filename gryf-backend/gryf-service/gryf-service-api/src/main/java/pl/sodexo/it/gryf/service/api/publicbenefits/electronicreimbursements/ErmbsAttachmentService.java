package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;

import java.util.List;

/**
 * Serwis do operacji na załącznikach dla bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface ErmbsAttachmentService {

    /**
     * Pobiera plik załącznik na podstawie ID
     * @param id - id załącznika
     * @return - dto załącznika
     */
    FileDTO getErmbsAttFileById(Long id);

    /**
     * Zapisuje dane załączniki dla danego rozliczenia
     * @param elctRmbsHeadDto - dto rozliczenia
     * @param status - stauts załącznika
     *
     * @return lista id załączników
     */
    List<Long> manageErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentStatus status);

    /**
     * Zapisuje dane załączniki dla danego rozliczenia
     * @param elctRmbsHeadDto - dto rozliczenia
     */
    void manageErmbsAttachmentsForCorrection(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentStatus status);

    /**
     * Sprawdza czy załacznik rozliczenia jest w ramach instytucji szkoleniowej zalogowanego zalogowanego użytkownika
     * @param ereimbursementAttachmentId
     * @return
     */
    boolean isEreimbursementAttachmentInLoggedUserInstitution(Long ereimbursementAttachmentId);

    /**
     * Zwraca Dto załączników rozliczenia na podstawie idków
     * @param attachmentsIds - identyfikatory rozliczenia
     * @return lista dto
     */
    List<ErmbsAttachmentDto> findErmbsAttachmentsByIds(List<Long> attachmentsIds);

}
