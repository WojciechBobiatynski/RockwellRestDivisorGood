package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.attachments.AttachmentFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Encja dla korekt rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 18.11.2016.
 */
@ToString
@Entity
@Table(name = "CORRECTION_ATTACHMENTS", schema = "APP_PBE")
@SequenceGenerator(name = "correc_att_seq", schema = "eagle", sequenceName = "correc_att_seq", allocationSize = 1)
public class CorrectionAttachment extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "correc_att_seq")
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CORR_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Correction correction;

    @ManyToOne
    @JoinColumn(name = "E_RMBS_ATT_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private ErmbsAttachment ermbsAttachment;

    @Column(name = "OLD_DOCUMENT_NUMBER")
    @Getter
    @Setter
    private String oldDocumentNumber;

    @Column(name = "OLD_ADDITIONAL_DESC")
    @Getter
    @Setter
    private String oldAdditionalDesc;

    @OneToOne
    @JoinColumn(name = "FILE_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private AttachmentFile attachmentFile;
}
