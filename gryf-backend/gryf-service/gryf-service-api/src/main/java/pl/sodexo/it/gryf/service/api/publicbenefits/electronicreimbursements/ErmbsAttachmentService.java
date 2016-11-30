package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;

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
     * @param ermbsAttachmentsDtoList - lista załączników
     */
    void saveErmbsAttachments(ElctRmbsHeadDto elctRmbsHeadDto, List<ErmbsAttachmentDto> ermbsAttachmentsDtoList);
}
