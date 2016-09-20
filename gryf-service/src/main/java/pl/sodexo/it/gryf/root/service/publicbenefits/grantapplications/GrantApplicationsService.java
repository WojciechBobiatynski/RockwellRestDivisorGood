package pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.detailsform.GrantApplicationDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.exception.StaleDataException;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications.GrantApplicationAttachmentRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications.GrantApplicationRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications.GrantApplicationVersionRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.root.service.FileService;
import pl.sodexo.it.gryf.root.service.MailService;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactType;
import pl.sodexo.it.gryf.utils.StringUtils;

/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@Service
@Transactional
public class GrantApplicationsService {

    //FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FileService fileService;

    @Autowired
    private GrantApplicationRepository applicationRepository;

    @Autowired
    private GrantApplicationVersionRepository applicationVersionRepository;

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private GrantApplicationAttachmentRepository grantApplicationAttachmentRepository;

    //PUBLIC METHODS

    public List<GrantApplicationSearchResultDTO> findApplications(GrantApplicationSearchQueryDTO searchDTO) {
        List<GrantApplication> applications = applicationRepository.findApplications(searchDTO);
        return GrantApplicationSearchResultDTO.createList(applications);
    }

    public String findApplicationFormData(Long id) {
        GrantApplication application = applicationRepository.get(id);
        GrantApplicationVersion applicationVersion = application.getApplicationVersion();
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        return grantApplicationService.findActualApplicationFormData(application);
    }

    public Long saveApplication(Long versionId, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.save(grantApplicationDTO);
        return application.getId();
    }

    public Long updateApplication(Long versionId, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.update(grantApplicationDTO);
        return application.getId();
    }

    public Long applyApplication(Long versionId, String data, List<FileDTO> fileDtoList, List<String> acceptedViolationsList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.apply(grantApplicationDTO, acceptedViolationsList);
        return application.getId();
    }

    public Long executeApplication(Long id, String data, List<FileDTO> fileDtoList, boolean checkVatRegNumDup) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.findByApplication(id);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.execute(id, grantApplicationDTO, checkVatRegNumDup);
        return application.getId();
    }

    public Long rejectApplication(Long id, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.findByApplication(id);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.reject(id, grantApplicationDTO);
        return application.getId();
    }

    /**
     * Wysyła maila z konkretnego szbalonu tworzy subject i body.
     * @param grantApplication
     * @param emailTemplateId
     * @param mailPlaceholders
     * @param emailsTo
     * @param attachments
     */
    public void sendPublicGrantProgramEmail (GrantApplication grantApplication, String emailTemplateId, MailService.MailPlaceholders mailPlaceholders,String emailsTo, List<MailAttachmentDTO> attachments){
        GrantApplicationVersion applicationVersion = grantApplication.getApplicationVersion();
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        grantApplicationService.sendPublicGrantProgramEmail(grantApplication, emailTemplateId, mailPlaceholders, emailsTo, attachments);
    }

    /**
     * Wysyła maila dla konkretych subject i body.
     * @param grantApplication
     * @param emailTemplateId
     * @param mailPlaceholders
     * @param emailsTo
     * @param attachments
     */
    public void sendPublicGrantProgramEmail (GrantApplication grantApplication, String emailTemplateId, String subject, String body, String emailsTo, List<MailAttachmentDTO> attachments){
        GrantApplicationVersion applicationVersion = grantApplication.getApplicationVersion();
        GrantApplicationService grantApplicationService = findGrantApplicationService(applicationVersion.getServiceBeanName());
        grantApplicationService.sendPublicGrantProgramEmail(grantApplication, emailTemplateId, subject, body, emailsTo, attachments);
    }

    public FileDTO getApplicationAttachmentFile(Long attachmentId) {
        GrantApplicationAttachment attachment = grantApplicationAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        manageLocking(id, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id, String message) {
        GrantApplication application = applicationRepository.get(id);
        throw new StaleDataException(application.getId(), application, message);
    }

    //DICTIONARIES

    public List<DictionaryDTO> FindGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return DictionaryDTO.createList(grantPrograms);
    }

    public List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(Long grantProgramId) {
        List<GrantApplicationVersion> applicationVersions = applicationVersionRepository.findByProgram(grantProgramId, new Date());
        return GrantApplicationVersionDictionaryDTO.createLists(applicationVersions);
    }

    
    //UTILITY METHODS
    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Wniosku. Tak zwrócony zbiór można sformatować przy pomocy funkcji 
     * {@link pl.sodexo.it.gryf.utils.GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param grantApplication przekazany obiekt wniosku
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego wniosku
     */
    public Set<String> getEmailRecipients(GrantApplication grantApplication, Set<String> existingRecipientsSet){
        Set<String> set;
        if (existingRecipientsSet == null){
            set = new HashSet<>();
        }
        else {
            set = existingRecipientsSet;
        }
        if (grantApplication != null) {
            GrantApplicationBasicData basicData = grantApplication.getBasicData();
            for (GrantApplicationContactData contact : basicData.getContacts()) {
                if (contact.getContactType() == GrantApplicationContactType.CONTACT) {
                    if (!StringUtils.isEmpty(contact.getEmail())) {
                        set.add(contact.getEmail());
                    }
                }
            }
        } 
        return set;
    }
    
    
    //PRIVATE METHODS

    private GrantApplicationService findGrantApplicationService(String serviceBeanName) {
        GrantApplicationV0BaseService service = (GrantApplicationV0BaseService) context.getBean(serviceBeanName);
        if (service == null) {
            throw new RuntimeException("Nie udało się pobrać serwisu o nazwie " + serviceBeanName);
        }
        return service;
    }

    
            
}
