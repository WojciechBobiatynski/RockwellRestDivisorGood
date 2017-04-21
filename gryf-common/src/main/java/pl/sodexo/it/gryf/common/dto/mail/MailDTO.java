package pl.sodexo.it.gryf.common.dto.mail;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@ToString(exclude = {"body", "attachments"})
public class MailDTO {

    //FIELDS

    private String templateId;
    
    private String subject;

    private String body;

    private String addressesFrom;

    private String addressesReplyTo;

    private String addressesTo;

    private String addressesCC;

    private EmailSourceType sourceType;

    private Long sourceId;

    private List<MailAttachmentDTO> attachments;

    private Long emailInstanceId;

    private Date delayTimestamp;

    //CONSTRUCTORS

    public MailDTO() {
    }

    public MailDTO(String templateId, String addressesTo, String subject, String body) {
        this.templateId = templateId;
        this.addressesTo = addressesTo;
        this.subject = subject;
        this.body = body;
    }

    public MailDTO(String templateId,
                   String subject, String body,
                   String addressesFrom, String addressesReplyTo, String addressesTo, String addressesCC,
                   EmailSourceType sourceType, Long sourceId,
                   List<MailAttachmentDTO> attachments) {
        this.templateId = templateId;
        this.subject = subject;
        this.body = body;
        this.addressesFrom = addressesFrom;
        this.addressesReplyTo = addressesReplyTo;
        this.addressesTo = addressesTo;
        this.addressesCC = addressesCC;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.attachments = attachments;
    }

    //GETTERS & SETTERS

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddressesFrom() {
        return addressesFrom;
    }

    public void setAddressesFrom(String addressesFrom) {
        this.addressesFrom = addressesFrom;
    }

    public String getAddressesReplyTo() {
        return addressesReplyTo;
    }

    public void setAddressesReplyTo(String addressesReplyTo) {
        this.addressesReplyTo = addressesReplyTo;
    }

    public String getAddressesTo() {
        return addressesTo;
    }

    public void setAddressesTo(String addressesTo) {
        this.addressesTo = addressesTo;
    }

    public String getAddressesCC() {
        return addressesCC;
    }

    public void setAddressesCC(String addressesCC) {
        this.addressesCC = addressesCC;
    }

    public EmailSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(EmailSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public List<MailAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MailAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Long getEmailInstanceId() {
        return emailInstanceId;
    }

    public void setEmailInstanceId(Long emailInstanceId) {
        this.emailInstanceId = emailInstanceId;
    }

    public Date getDelayTimestamp() {
        return delayTimestamp;
    }

    public void setDelayTimestamp(Date delayTimestamp) {
        this.delayTimestamp = delayTimestamp;
    }
}
