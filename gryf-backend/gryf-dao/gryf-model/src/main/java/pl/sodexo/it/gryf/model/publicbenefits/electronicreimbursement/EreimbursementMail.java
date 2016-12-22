package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.enums.ErmbsMailType;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.mail.EmailInstance;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-12-20.
 */
@Entity
@Table(name = "E_REIMBURSEMENT_EMAILS", schema = "APP_PBE")
@ToString
public class EreimbursementMail extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    private Long id;

    @JoinColumn(name = "E_REIMBURSEMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    @Getter
    @Setter
    private Ereimbursement ereimbursement;

    @JoinColumn(name = "EMAIL_INSTANCE_ID", referencedColumnName = "ID")
    @OneToOne
    @Getter
    @Setter
    private EmailInstance emailInstance;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMAIL_TYPE")
    @Getter
    @Setter
    private ErmbsMailType emailType;

    @Column(name = "EMAILS_TO")
    @Getter
    @Setter
    private String emailsTo;

    @Column(name = "EMAIL_SUBJECT")
    @Getter
    @Setter
    private String emailSubject;

    @Column(name = "EMAIL_BODY")
    @Getter
    @Setter
    private String emailBody;
}
