package pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;

import java.util.List;
import java.util.Set;

/**
 * Serwis  obsługujący wysyłkę maili dla wniosków
 *
 * Created by jbentyn on 2016-09-27.
 */
public interface GrantApplicationEmailService {

    /**
     * Wysyła maila z konkretnego szbalonu tworzy subject i body.
     *
     * @param grantApplication
     * @param emailTemplateId
     * @param mailPlaceholders
     * @param emailsTo
     * @param attachments
     */
    void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId, MailPlaceholders mailPlaceholders, String emailsTo, List<MailAttachmentDTO> attachments);

    /**
     * Wysyła maila dla konkretych subject i body.
     *
     * @param grantApplication
     * @param emailTemplateId
     * @param subject
     * @param body
     * @param emailsTo
     * @param attachments
     */
    void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId, String subject, String body, String emailsTo, List<MailAttachmentDTO> attachments);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Wniosku. Tak zwrócony zbiór można sformatować przy pomocy funkcji
     * {@link GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     *
     * @param grantApplication przekazany obiekt wniosku
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego wniosku
     */
    Set<String> getEmailRecipients(GrantApplication grantApplication, Set<String> existingRecipientsSet);

    //TODO wydzielić do jakiegoś utilsa
    GrantApplicationService findGrantApplicationService(String serviceBeanName);
}
