package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.mail.EmailInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementMail;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Mapper mapujący dto ErmbsAttachmentDto na encję ErmbsAttachment
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class ErmbsMailAttachmentDtoMapper extends VersionableDtoMapper<ErmbsMailDto, EreimbursementMail> {

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private EmailInstanceRepository emailInstanceRepository;

    @Override
    protected EreimbursementMail initDestination() {
        return new EreimbursementMail();
    }

    @Override
    protected void map(ErmbsMailDto dto, EreimbursementMail entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setEreimbursement(dto.getErmbsId() != null ? ereimbursementRepository.get(dto.getErmbsId()) : null);
        entity.setEmailInstance(dto.getEmailInstanceId() != null ? emailInstanceRepository.get(dto.getEmailInstanceId()) : null);
        entity.setEmailType(dto.getEmailType());
        entity.setEmailSubject(dto.getEmailSubject());
        entity.setEmailBody(dto.getEmailBody());
        entity.setEmailsTo(dto.getEmailsTo());
    }

}
