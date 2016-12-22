package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailAttachmentDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailParamsDto;
import pl.sodexo.it.gryf.common.enums.ErmbsMailType;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementMailRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementMail;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsMailService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.ErmbsMailAttachmentDtoMapper;

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

    private static final int MAILS_FROM_TEMPLATE_NUM = 2;

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

    @Override
    public List<ErmbsMailDto> createMailFromTemplates(Long ermbsId) {
        List<ErmbsMailDto> existedMail = electronicReimbursementsDao.findMailsByErmbsId(ermbsId);
        if(existedMail.isEmpty()){
            return createAllMailFromTemplates(ermbsId);
        } else if(existedMail.size() == MAILS_FROM_TEMPLATE_NUM) {
            return null;
        } else {
            List<ErmbsMailDto> mails = new ArrayList<>();
            addReimbConfMailIfNotExist(ermbsId, existedMail, mails);
            addPayConfirmMailIfNotExist(ermbsId, existedMail, mails);
            return mails;
        }
    }

    private void addPayConfirmMailIfNotExist(Long ermbsId, List<ErmbsMailDto> existedMail, List<ErmbsMailDto> mails) {
        if(existedMail.stream().noneMatch(ermbsMailDto -> ErmbsMailType.PAYMENT_CONFIRMATION.equals(ermbsMailDto.getEmailType()))){
            ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParams(ermbsId);
            List<ErmbsMailAttachmentDto> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);
            ErmbsMailDto confirmPaymentMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmPaymentMailDto(paramsDto), ermbsId);
            confirmPaymentMail.setEmailType(ErmbsMailType.PAYMENT_CONFIRMATION);
            addReportToConfirmPaymentMailsAsAttachments(confirmPaymentMail, reportFiles);
            mails.add(confirmPaymentMail);
        }
    }

    private void addReimbConfMailIfNotExist(Long ermbsId, List<ErmbsMailDto> existedMail, List<ErmbsMailDto> mails) {
        if(existedMail.stream().noneMatch(ermbsMailDto -> ErmbsMailType.E_REIMBURSEMENT_CONFIRMATION.equals(ermbsMailDto.getEmailType()))){
            ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParams(ermbsId);
            List<ErmbsMailAttachmentDto> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);
            ErmbsMailDto confirmReimbMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmReimbMailDto(paramsDto), ermbsId);
            confirmReimbMail.setEmailType(ErmbsMailType.E_REIMBURSEMENT_CONFIRMATION);
            addReportToConfirmReimbMailsAsAttachments(confirmReimbMail, reportFiles);
            mails.add(confirmReimbMail);
        }
    }

    private List<ErmbsMailDto> createAllMailFromTemplates(Long ermbsId) {
        List<ErmbsMailDto> mails = new ArrayList<>();
        ErmbsMailParamsDto paramsDto = electronicReimbursementsDao.findMailParams(ermbsId);
        List<ErmbsMailAttachmentDto> reportFiles = electronicReimbursementsDao.findReportsByErmbsId(ermbsId);

        ErmbsMailDto confirmPaymentMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmPaymentMailDto(paramsDto), ermbsId);
        confirmPaymentMail.setEmailType(ErmbsMailType.PAYMENT_CONFIRMATION);
        addReportToConfirmPaymentMailsAsAttachments(confirmPaymentMail, reportFiles);
        mails.add(confirmPaymentMail);

        ErmbsMailDto confirmReimbMail = createErmbsMailFromMailDTO(mailDtoCreator.createConfirmReimbMailDto(paramsDto), ermbsId);
        confirmReimbMail.setEmailType(ErmbsMailType.E_REIMBURSEMENT_CONFIRMATION);
        addReportToConfirmReimbMailsAsAttachments(confirmReimbMail, reportFiles);
        mails.add(confirmReimbMail);

        return mails;
    }

    @Override
    public ErmbsMailDto sendErmbsMail(ErmbsMailDto dto) {
        //TODO: AK - uporządkować
        MailDTO mail = mailDtoCreator.createMailDTOForEreimbMail(dto);
        fillMailDTOWithAttachmentsOfErmbsMail(mail, dto);
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

    private void addReportToConfirmPaymentMailsAsAttachments(ErmbsMailDto mail, List<ErmbsMailAttachmentDto> reportFiles){
        ErmbsMailAttachmentDto att = reportFiles.stream().filter(dto -> ReportTemplateCode.GRANT_AID_CONFIRMATION.getTypeName().equals(dto.getName())).findFirst().get();
        att.setReportFile(true);
        mail.getAttachments().add(att);
    }

    private void addReportToConfirmReimbMailsAsAttachments(ErmbsMailDto mail, List<ErmbsMailAttachmentDto> reportFiles){
        ErmbsMailAttachmentDto att = reportFiles.stream().filter(dto -> ReportTemplateCode.BANK_TRANSFER_CONFIRMATION.getTypeName().equals(dto.getName())).findFirst().get();
        att.setReportFile(true);
        mail.getAttachments().add(att);
    }

    private MailDTO fillMailDTOWithAttachmentsOfErmbsMail(MailDTO mail, ErmbsMailDto dto){
        List<MailAttachmentDTO> mailAttachments = new ArrayList<>();
        dto.getAttachments().stream().forEach(ermbsMailAttachmentDto -> {
            MailAttachmentDTO att = new MailAttachmentDTO();
            if(ermbsMailAttachmentDto.isReportFile()){
                //TODO: AK - skopiować plik do nowej ścieżki i dopiero załączyć do maila
                att.setPath(ermbsMailAttachmentDto.getFileLocation());
                att.setName(ermbsMailAttachmentDto.getName());
            } else {
                String newFileName = GryfStringUtils.convertFileName(ermbsMailAttachmentDto.getOriginalFilename());
                String path = fileService.writeFile(FileType.E_REIMBURSEMENTS, newFileName, ermbsMailAttachmentDto.getFile(), null);
                att.setPath(path);
                att.setName(ermbsMailAttachmentDto.getName());
            }
            mailAttachments.add(att);
        });
        mail.setAttachments(mailAttachments);
        return mail;
    }
}
