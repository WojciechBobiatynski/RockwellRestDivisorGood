package pl.sodexo.it.gryf.service.api.attachments;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;

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

    /**
     * Zapisuje pliki związane z rozliczeniem, gdy pracujemy w trybie korekty
     *
     * @param elctRmbsHeadDto - dto rozliczenia
     */
    void manageAttachmentFilesForCorrections(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Zarządza plikiem załącznika
     * @param elctRmbsHeadDto - dto rozliczenia
     * @param ermbsAttachment - dto załącznika rozliczenia
     */
    void manageFile(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachment);

}
