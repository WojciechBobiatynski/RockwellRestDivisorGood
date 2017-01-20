package pl.sodexo.it.gryf.model.mail;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.mail.EmailSourceType;
import pl.sodexo.it.gryf.model.api.CreationAuditedEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@ToString(exclude = {"emailTemplate", "sourceType", "attachments"})
@Entity
@Table(name = "EMAIL_INSTANCES", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "EmailInstance.findByStatus", query = "select e from EmailInstance e where e.status = :status and (e.delayTimestamp is null or e.delayTimestamp <= current_timestamp) order by e.createdUser"),
})
public class EmailInstance extends CreationAuditedEntity{

    //STATIC FIELDS

    public static final String STATUS_DONE = "D";
    public static final String STATUS_ERROR = "E";
    public static final String STATUS_PENDING = "P";

    public static final int EMAIL_SUBJECT_MAX_SIZE = 200;
    public static final int EMAIL_FROM_MAX_SIZE = 100;
    public static final int EMAILS_REPLY_TO_MAX_SIZE = 100;
    public static final int EMAILS_TO_MAX_SIZE = 100;
    public static final int EMAILS_CC_MAX_SIZE = 100;
    public static final int MAIL_SESSION_PROPERTIES_MAX_SIZE = 100;
    public static final int ERROR_MESSAGE_MAX_SIZE = 100;

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMAIL_TEMPLATE_ID", referencedColumnName = "ID")
    private EmailTemplate emailTemplate;

    @Column(name = "EMAIL_DATA")
    private String emailData;

    @Column(name = "EMAIL_SUBJECT")
    private String emailSubject;

    @Column(name = "EMAILS_TO")
    private String emailsTo;

    @Column(name = "MAIL_SESSION_PROPERTIES")
    private String mailSessionProperties;

    @Column(name = "STATUS")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMAIL_TYPE")
    private EmailType emailType;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE_TYPE")
    private EmailSourceType sourceType;

    @Column(name = "SOURCE_ID")
    private Long sourceId;

    @Column(name = "DELAY_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date delayTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emailInstance")
    private List<EmailInstanceAttachment> attachments;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public String getEmailData() {
        return emailData;
    }

    public void setEmailData(String emailBody) {
        this.emailData = emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailsTo() {
        return emailsTo;
    }

    public void setEmailsTo(String emailsTo) {
        this.emailsTo = emailsTo;
    }

    public String getMailSessionProperties() {
        return mailSessionProperties;
    }

    public void setMailSessionProperties(String mailSessionProperties) {
        this.mailSessionProperties = mailSessionProperties;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public Date getDelayTimestamp() {
        return delayTimestamp;
    }

    public void setDelayTimestamp(Date delayTimestamp) {
        this.delayTimestamp = delayTimestamp;
    }

    //LIST METHODS

    private List<EmailInstanceAttachment> getInitializedAttachments() {
        if (attachments == null)
            attachments = new ArrayList<>();
        return attachments;
    }

    public List<EmailInstanceAttachment> getAttachments() {
        return Collections.unmodifiableList(getInitializedAttachments());
    }

    public void addAttachment(EmailInstanceAttachment attachment) {
        if (attachment.getEmailInstance() != null && attachment.getEmailInstance() != this) {
            attachment.getEmailInstance().getInitializedAttachments().remove(attachment);
        }
        if (attachment.getId() == null || !getInitializedAttachments().contains(attachment)) {
            getInitializedAttachments().add(attachment);
        }
        attachment.setEmailInstance(this);
    }

    public void removeAttachment(EmailInstanceAttachment attachment) {
        getInitializedAttachments().remove(attachment);
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((EmailInstance) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
