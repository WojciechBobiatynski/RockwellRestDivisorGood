package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailParamsDto;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsMailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Serwis do operacji na mailach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class ErmbsMailServiceImpl implements ErmbsMailService {

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    @Autowired
    private MailDtoCreator mailDtoCreator;

    @Override
    public List<ErmbsMailDto> createMailFromTemplates(Long ermbsId) {
        List<ErmbsMailDto> mails = new ArrayList<>();
        ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParams(ermbsId);
        List<FileDTO> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);

        ErmbsMailDto confirmPaymentMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmPaymentMailDto(paramsDto), ermbsId);
        addReportToConfirmPaymentMailsAsAttachments(confirmPaymentMail, reportFiles);
        mails.add(confirmPaymentMail);

        ErmbsMailDto confirmReimbMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmReimbMailDto(paramsDto), ermbsId);
        addReportToConfirmReimbMailsAsAttachments(confirmReimbMail, reportFiles);
        mails.add(confirmReimbMail);

        return mails;
    }

    private ErmbsMailDto createErmbsMailFromMailDTO(MailDTO mailDTO, Long ermbsId){
        ErmbsMailDto ermbsMailDto = new ErmbsMailDto();
        ermbsMailDto.setErmbsId(ermbsId);
        ermbsMailDto.setEmailBody(mailDTO.getBody());
        ermbsMailDto.setEmailsTo(mailDTO.getAddressesTo());
        ermbsMailDto.setEmailSubject(mailDTO.getSubject());
        return ermbsMailDto;
    }

    private void addReportToConfirmPaymentMailsAsAttachments(ErmbsMailDto mail, List<FileDTO> reportFiles){
        mail.getAttachments().add(reportFiles.stream().filter(fileDTO -> ReportTemplateCode.GRANT_AID_CONFIRMATION.getTypeName().equals(fileDTO.getName())).findFirst().get());
    }

    private void addReportToConfirmReimbMailsAsAttachments(ErmbsMailDto mail, List<FileDTO> reportFiles){
        mail.getAttachments().add(reportFiles.stream().filter(fileDTO -> ReportTemplateCode.BANK_TRANSFER_CONFIRMATION.getTypeName().equals(fileDTO.getName())).findFirst().get());
    }
}
