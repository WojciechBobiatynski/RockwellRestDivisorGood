package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailAttachmentDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailParamsDto;
import pl.sodexo.it.gryf.common.enums.ErmbsMailType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementMailRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementMail;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsMailService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsMailAttachmentDtoMapper;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Serwis do operacji na mailach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class ErmbsMailServiceImpl implements ErmbsMailService {

    private static final int DAYS_OF_PAYMENT_CONFIRMATION_EMAIL_SENDING_DELAY = 1;
    private static final int DEFAULT_MAILS_FROM_TEMPLATE_NUM = 1;

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    @Autowired
    private MailDtoCreator mailDtoCreator;

    @Autowired
    private MailService mailService;

    @Autowired
    private ErmbsMailAttachmentDtoMapper ermbsMailAttachmentDtoMapper;

    @Autowired
    private EreimbursementMailRepository ereimbursementMailRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    public List<ErmbsMailDto> createMailFromTemplates(Long ermbsId) {
        List<ErmbsMailDto> existedMail = electronicReimbursementsDao.findMailsByErmbsId(ermbsId);
        if(existedMail.size() == getMailsFromTemplateNumForErmbs(ermbsId)){
            return null;
        } else {
            return createMailsFromTemplates(ermbsId, existedMail);
        }
    }

    @Override
    public List<ErmbsMailDto> createMailFromTemplatesForUnreservedPool(Long ermbsId) {
        List<ErmbsMailDto> existedMail = electronicReimbursementsDao.findMailsByErmbsId(ermbsId);
        if(existedMail.size() == DEFAULT_MAILS_FROM_TEMPLATE_NUM){
            return null;
        } else {
            return createMailsFromTemplatesForUnreservedPool(ermbsId, existedMail);
        }
    }

    private List<ErmbsMailDto> createMailsFromTemplatesForUnreservedPool(Long ermbsId, List<ErmbsMailDto> existedMail) {
        ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParamsForUnreservedPool(ermbsId);
        List<ErmbsMailAttachmentDto> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);

        addCreditNoteMailIfNotExistForUnreservedPool(ermbsId, existedMail, paramsDto, reportFiles);

        return existedMail;
    }

    private void addCreditNoteMailIfNotExistForUnreservedPool(Long ermbsId, List<ErmbsMailDto> existedMail, ErmbsMailParamsDto paramsDto, List<ErmbsMailAttachmentDto> reportFiles) {
        if(existedMail.stream().noneMatch(ermbsMailDto -> ErmbsMailType.CREDIT_NOTE.equals(ermbsMailDto.getEmailType())) && electronicReimbursementsDao.shouldBeCreditNoteCreated(ermbsId)){
            ErmbsMailDto confirmReimbMail = createErmbsMailFromMailDTO(mailDtoCreator.createCreditNoteForUnreservedPoolMailDto(paramsDto), ermbsId);
            confirmReimbMail.setEmailType(ErmbsMailType.CREDIT_NOTE);
            addReportToCreditNoteMailAsAttachments(confirmReimbMail, reportFiles);
            existedMail.add(confirmReimbMail);
        }
    }

    private int getMailsFromTemplateNumForErmbs(Long ermbsId){
        int mailsFromTemplate = DEFAULT_MAILS_FROM_TEMPLATE_NUM;
        if(electronicReimbursementsDao.shouldBeCreditNoteCreated(ermbsId)) {
            mailsFromTemplate++;
        }
        return mailsFromTemplate;
    }

    private List<ErmbsMailDto> createMailsFromTemplates(Long ermbsId, List<ErmbsMailDto> existedMail) {
        ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParams(ermbsId);
        List<ErmbsMailAttachmentDto> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);

        addPayConfirmMailIfNotExist(ermbsId, existedMail, paramsDto, reportFiles);
        addCreditNoteMailIfNotExist(ermbsId, existedMail, paramsDto, reportFiles);

        return existedMail;
    }

    private void addPayConfirmMailIfNotExist(Long ermbsId, List<ErmbsMailDto> existedMail, ErmbsMailParamsDto paramsDto, List<ErmbsMailAttachmentDto> reportFiles) {
        if(existedMail.stream().noneMatch(ermbsMailDto -> ErmbsMailType.PAYMENT_CONFIRMATION.equals(ermbsMailDto.getEmailType()))){
            ErmbsMailDto confirmPaymentMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmPaymentMailDto(paramsDto), ermbsId);
            confirmPaymentMail.setEmailType(ErmbsMailType.PAYMENT_CONFIRMATION);
            addReportToConfirmPaymentMailsAsAttachments(confirmPaymentMail, reportFiles, paramsDto);
            existedMail.add(confirmPaymentMail);
        }
    }

    private void addCreditNoteMailIfNotExist(Long ermbsId, List<ErmbsMailDto> existedMail, ErmbsMailParamsDto paramsDto, List<ErmbsMailAttachmentDto> reportFiles) {
        if(existedMail.stream().noneMatch(ermbsMailDto -> ErmbsMailType.CREDIT_NOTE.equals(ermbsMailDto.getEmailType())) && electronicReimbursementsDao.shouldBeCreditNoteCreated(ermbsId)){
            ErmbsMailDto confirmReimbMail = createErmbsMailFromMailDTO(mailDtoCreator.createCreditNoteMailDto(paramsDto), ermbsId);
            confirmReimbMail.setEmailType(ErmbsMailType.CREDIT_NOTE);
            addReportToCreditNoteMailAsAttachments(confirmReimbMail, reportFiles);
            existedMail.add(confirmReimbMail);
        }
    }

    @Override
    public ErmbsMailDto sendErmbsMail(ErmbsMailDto dto) {
        //TODO: AK - uporządkować
        MailDTO mail = mailDtoCreator.createMailDTOForEreimbMail(dto);
        fillMailDTOWithAttachmentsOfErmbsMail(mail, dto);
        Date delayTimestamp = new Date();
        if(ErmbsMailType.PAYMENT_CONFIRMATION.equals(dto.getEmailType())){
            LocalDateTime localDateTime = delayTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            delayTimestamp = Date.from(localDateTime.plusDays(DAYS_OF_PAYMENT_CONFIRMATION_EMAIL_SENDING_DELAY).atZone(ZoneId.systemDefault()).toInstant());
        }
        mail.setDelayTimestamp(delayTimestamp);
        mail = mailService.scheduleMail(mail);
        dto.setEmailInstanceId(mail.getEmailInstanceId());
        EreimbursementMail entity = ermbsMailAttachmentDtoMapper.convert(dto);
        ereimbursementMailRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    private ErmbsMailDto createErmbsMailFromMailDTO(MailDTO mailDTO, Long ermbsId){
        ErmbsMailDto ermbsMailDto = new ErmbsMailDto();
        ermbsMailDto.setErmbsId(ermbsId);
        ermbsMailDto.setEmailBody(mailDTO.getBody());
        ermbsMailDto.setEmailsTo(mailDTO.getAddressesTo());
        ermbsMailDto.setEmailSubject(mailDTO.getSubject());
        return ermbsMailDto;
    }

    private void addReportToConfirmPaymentMailsAsAttachments(ErmbsMailDto mail, List<ErmbsMailAttachmentDto> reportFiles, ErmbsMailParamsDto paramsDto){
        ErmbsMailAttachmentDto bankTransferConfirmationAtt = reportFiles.stream().filter(dto -> ReportTemplateCode.BANK_TRANSFER_CONFIRMATION.getTypeName().equals(dto.getName())).findFirst().get();
        bankTransferConfirmationAtt.setReportFile(true);
        mail.getAttachments().add(bankTransferConfirmationAtt);
        if(paramsDto.isContractForEnterprise()){
            ErmbsMailAttachmentDto grantAidConfirmationAtt = reportFiles.stream().filter(dto -> ReportTemplateCode.GRANT_AID_CONFIRMATION.getTypeName().equals(dto.getName())).findFirst().get();
            grantAidConfirmationAtt.setReportFile(true);
            mail.getAttachments().add(grantAidConfirmationAtt);
        }
    }

    private void addReportToCreditNoteMailAsAttachments(ErmbsMailDto mail, List<ErmbsMailAttachmentDto> reportFiles){
        ErmbsMailAttachmentDto att = reportFiles.stream().filter(dto -> ReportTemplateCode.CREDIT_NOTE.getTypeName().equals(dto.getName())).findFirst().get();
        att.setReportFile(true);
        mail.getAttachments().add(att);
    }

    private MailDTO fillMailDTOWithAttachmentsOfErmbsMail(MailDTO mail, ErmbsMailDto dto){
        List<MailAttachmentDTO> mailAttachments = new ArrayList<>();
        dto.getAttachments().stream().forEach(ermbsMailAttachmentDto -> {
            MailAttachmentDTO att = new MailAttachmentDTO();
            String newFileRoot = getErmbsEmailAttachmentDirectory();
            if(ermbsMailAttachmentDto.isReportFile()){
                String newFilePath = fileService.copyFile(ermbsMailAttachmentDto.getFileLocation(), newFileRoot);
                att.setPath(newFilePath);
                att.setName(Paths.get(newFilePath).getFileName().toString());
            } else {
                String newFilePath = fileService.writeFile(ermbsMailAttachmentDto.getFile(), newFileRoot);
                att.setPath(newFilePath);
                att.setName(Paths.get(newFilePath).getFileName().toString());
            }
            mailAttachments.add(att);
        });
        mail.setAttachments(mailAttachments);
        return mail;
    }

    private String getErmbsEmailAttachmentDirectory() {
        return applicationParameters.getPathAttachments() + applicationParameters.getPathEreimbursements() + applicationParameters.getErmbsEmailAttachmentDirectory();
    }
}
