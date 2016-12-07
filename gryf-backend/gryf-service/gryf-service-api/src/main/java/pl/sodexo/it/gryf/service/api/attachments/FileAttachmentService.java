package pl.sodexo.it.gryf.service.api.attachments;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;

/**
 * Interfejs do operacji na plikach załączników
 *
 * Created by akmiecinski on 07.12.2016.
 */
public interface FileAttachmentService {

    /**
     * Zapisuje pliki związane z rozliczeniem
     *
     * @param elctRmbsHeadDto - dto rozliczenia
     */
    void manageAttachmentFiles(ElctRmbsHeadDto elctRmbsHeadDto);

}
