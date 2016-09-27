package pl.sodexo.it.gryf.model.mail;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@ToString
@Entity
@Table(name = "EMAIL_INSTANCE_ATTACHMENTS", schema = "APP_PBE")
public class EmailInstanceAttachment extends GryfEntity{

    //STATIC FIELDS

    public static final int ATTACHMENT_NAME_MAX_SIZE = 50;

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMAIL_INSTANCE_ID", referencedColumnName = "ID")
    private EmailInstance emailInstance;

    @Column(name = "ATTACHMENT_PATH")
    private String attachmentPath;

    @Column(name = "ATTACHMENT_NAME")
    private String attachmentName;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailInstance getEmailInstance() {
        return emailInstance;
    }

    public void setEmailInstance(EmailInstance emailInstance) {
        this.emailInstance = emailInstance;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
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
        return Objects.equals(id, ((EmailInstanceAttachment) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
