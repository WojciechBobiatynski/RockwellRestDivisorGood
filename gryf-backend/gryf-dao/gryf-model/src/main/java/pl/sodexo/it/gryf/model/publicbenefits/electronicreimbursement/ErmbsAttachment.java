package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.attachments.AttachmentType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Encja dla załączników do rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 22.11.2016.
 */
@ToString(exclude = {"ereimbursement", "correction", "attachmentType"})
@Entity
@Table(name = "E_RMBS_ATTACHMENTS", schema = "APP_PBE")
@SequenceGenerator(name="ermbs_attach_seq", schema = "eagle", sequenceName = "ermbs_attach_seq", allocationSize = 1)
public class ErmbsAttachment extends VersionableEntity{

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "ermbs_attach_seq")
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "E_RMBS_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Ereimbursement ereimbursement;

    @ManyToOne
    @JoinColumn(name = "CORR_ID", referencedColumnName = "ID")
    @Getter
    @Setter
    private Correction correction;

    @ManyToOne
    @JoinColumn(name = "ATTACH_TYPE", referencedColumnName = "CODE")
    @Getter
    @Setter
    private AttachmentType attachmentType;

    @Column(name = "DOCUMENT_NUMBER")
    @Getter
    @Setter
    private String documentNumber;

    @Column(name = "ADDITIONAL_DESCRIPTION")
    @Getter
    @Setter
    private String additionalDescription;

    @Column(name = "ORGINAL_FILE_NAME")
    @Getter
    @Setter
    private String orginalFileName;

    @Column(name = "FILE_LOCATION")
    @Getter
    @Setter
    private String fileLocation;

}
