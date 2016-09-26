package pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;

import java.util.List;
import java.util.Set;

/**
 * Created by jbentyn on 2016-09-20.
 */
//TODO zmienic nazwe na mniej podobną do GrantApplicationService
public interface GrantApplicationsService {

    List<GrantApplicationSearchResultDTO> findApplications(GrantApplicationSearchQueryDTO searchDTO);

    String findApplicationFormData(Long id);

    Long saveApplication(Long versionId, String data, List<FileDTO> fileDtoList);

    Long updateApplication(Long versionId, String data, List<FileDTO> fileDtoList);

    Long applyApplication(Long versionId, String data, List<FileDTO> fileDtoList, List<String> acceptedViolationsList);

    Long executeApplication(Long id, String data, List<FileDTO> fileDtoList, boolean checkVatRegNumDup);

    Long rejectApplication(Long id, String data, List<FileDTO> fileDtoList);

    /**
     * Wysyła maila z konkretnego szbalonu tworzy subject i body.
     * @param grantApplication
     * @param emailTemplateId
     * @param mailPlaceholders
     * @param emailsTo
     * @param attachments
     */
    void sendPublicGrantProgramEmail (GrantApplication grantApplication, String emailTemplateId, MailPlaceholders mailPlaceholders, String emailsTo, List<MailAttachmentDTO> attachments);

    /**
     * Wysyła maila dla konkretych subject i body.
     * @param grantApplication
     * @param emailTemplateId
     * @param subject
     * @param body
     * @param emailsTo
     * @param attachments
     */
    void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId, String subject, String body, String emailsTo, List<MailAttachmentDTO> attachments);

    FileDTO getApplicationAttachmentFile(Long attachmentId);

    void manageLocking(Long id);

    void manageLocking(Long id, String message);

    List<DictionaryDTO> FindGrantProgramsDictionaries();

    List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(Long grantProgramId);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Wniosku. Tak zwrócony zbiór można sformatować przy pomocy funkcji
     * {@link GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param grantApplication przekazany obiekt wniosku
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego wniosku
     */
    Set<String> getEmailRecipients(GrantApplication grantApplication, Set<String> existingRecipientsSet);
}
