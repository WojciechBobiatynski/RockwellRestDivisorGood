package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;

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
     */
    void saveErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Zapisuje dane załączniki dla danego rozliczenia z korektą
     * @param elctRmbsHeadDto - dto rozliczenia
     */
    void saveErmbsAttachmentsForCorr(ElctRmbsHeadDto elctRmbsHeadDto);
}
