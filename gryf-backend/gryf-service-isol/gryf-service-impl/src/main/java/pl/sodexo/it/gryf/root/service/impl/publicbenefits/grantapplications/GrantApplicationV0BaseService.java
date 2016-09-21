package pl.sodexo.it.gryf.root.service.impl.publicbenefits.grantapplications;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.*;
import pl.sodexo.it.gryf.model.FileType;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.mail.EmailSourceType;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseEntityType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.*;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.root.repository.dictionaries.ContactTypeRepository;
import pl.sodexo.it.gryf.root.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.enterprises.EnterpriseEntityTypeRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.enterprises.EnterpriseSizeRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications.*;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms.GrantProgramParamRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.root.service.ApplicationParametersService;
import pl.sodexo.it.gryf.root.service.local.FileService;
import pl.sodexo.it.gryf.root.service.local.MailService;
import pl.sodexo.it.gryf.root.service.local.ValidateService;
import pl.sodexo.it.gryf.root.service.local.publicbenefits.grantapplications.GrantApplicationService;
import pl.sodexo.it.gryf.root.service.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.MailPlaceholders;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;

import javax.mail.Session;
import java.util.*;


/**
 * Created by tomasz.bilski.ext on 2015-06-25.
 */
public abstract class GrantApplicationV0BaseService<T extends GrantApplicationV0BaseDTO> implements GrantApplicationService<T> {

    //FINAL FILEDS

    private final Map<String, Set<String>> statusMap;
    private final int REJECTION_REASON_LENGTH_LIMIT = 950;

    //FIELDS

    @Autowired
    protected ValidateService validateService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected ApplicationParametersService applicationParametersService;

    @Autowired
    protected EnterpriseService enterpriseService;

    @Autowired
    protected OrderService orderService;
    
    @Autowired
    protected GrantApplicationsService grantApplicationsService;

    @Autowired
    protected MailService mailService;

    @Autowired
    protected GrantApplicationRepository applicationRepository;

    @Autowired
    protected GrantApplicationVersionRepository applicationVersionRepository;

    @Autowired
    protected EnterpriseRepository enterpriseRepository;

    @Autowired
    protected ZipCodeRepository zipCodeRepository;

    @Autowired
    protected GrantApplicationStatusRepository applicationStatusRepository;

    @Autowired
    protected EnterpriseSizeRepository enterpriseSizeRepository;

    @Autowired
    protected EnterpriseEntityTypeRepository enterpriseEntityTypeRepository;

    @Autowired
    protected GrantApplicationAttachmentRepository applicationAttachmentRepository;

    @Autowired
    protected GrantApplicationPkdRepository applicationPkdRepository;

    @Autowired
    protected GrantApplicationContactDataRepository applicationContactDataRepository;

    @Autowired
    protected GrantApplicationRepDataRepository applicationRepDataRepository;

    @Autowired
    protected GrantApplicationRepHeaderRepository applicationRepHeaderRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ContactTypeRepository contactTypeRepository;

    @Autowired
    protected GrantProgramRepository grantProgramRepository;

    @Autowired
    protected GrantProgramParamRepository grantProgramParamRepository;

    //CONSTRUCTOR

    public GrantApplicationV0BaseService(){
        statusMap = new HashMap<>();

        Set<String> setNull = new HashSet<>();
        setNull.add(GrantApplicationStatus.NEW_CODE);
        setNull.add(GrantApplicationStatus.APPLIED_CODE);
        statusMap.put(null, setNull);

        Set<String> setNew = new HashSet<>();
        setNew.add(GrantApplicationStatus.APPLIED_CODE);
        statusMap.put(GrantApplicationStatus.NEW_CODE, setNew);

        Set<String> setApplied = new HashSet<>();
        setApplied.add(GrantApplicationStatus.EXECUTED_CODE);
        setApplied.add(GrantApplicationStatus.REJECTED_CODE);
        statusMap.put(GrantApplicationStatus.APPLIED_CODE, setApplied);

        Set<String> setExecuted = new HashSet<>();
        statusMap.put(GrantApplicationStatus.EXECUTED_CODE, setExecuted);

        Set<String> setRejected = new HashSet<>();
        statusMap.put(GrantApplicationStatus.REJECTED_CODE, setRejected);
    }

    //PUBLIC METHODS

    public String findActualApplicationFormData(GrantApplication application) {

        GrantApplicationVersion applicationVersion = application.getApplicationVersion();
        GrantApplicationFormData applicationFormData = application.getFormData();

        T dto = (T)GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), applicationFormData.getFormData());
        dto = fillApplicationDto(dto, application);

        return GrantApplicationParser.writeApplicationData(dto);
    }

    public GrantApplication save(T dto){

        //STATUS
        if(dto.getStatus() == null || dto.getStatus().getId() == null) {
            GrantApplicationStatus status = applicationStatusRepository.get(GrantApplicationStatus.NEW_CODE);
            dto.setStatus(DictionaryDTO.create(status));
        }

        //ACTION
        validate(dto, ValidationGroupNewGrantApplication.class);
        GrantApplication application = saveApplication(dto);
        saveBasicData(application, dto);
        saveFormData(application, dto);
        return application;
    }

    public GrantApplication update(T dto){

        //STATUS
        if(dto.getStatus() == null) {
            GrantApplicationStatus status = applicationStatusRepository.get(GrantApplicationStatus.NEW_CODE);
            dto.setStatus(DictionaryDTO.create(status));
        }

        //ACTION
        validate(dto, ValidationGroupNewGrantApplication.class);
        GrantApplication application = updateApplication(dto);
        updateBasicData(application, dto);
        updateFormData(application, dto);
        return application;
    }

    public GrantApplication apply(T dto, List<String> acceptedPathViolations) {
        //STATUS
        GrantApplicationStatus status = applicationStatusRepository.get(GrantApplicationStatus.APPLIED_CODE);
        dto.setStatus(DictionaryDTO.create(status));
        dto.setApplyDate(new Date());

        //ACTION
        GrantApplication application;
        if (dto.getId() == null) {
            validate(dto, ValidationGroupApplyMandatoryApplication.class);
            List<EntityConstraintViolation> acceptedViolations = validateWithConfirm(dto, acceptedPathViolations, ValidationGroupApplyOptionalApplication.class);
            dto.setRejectionReason(getRejectionReasonText(acceptedViolations));

            application = saveApplication(dto);
            saveBasicData(application, dto);
            saveReportData(application, dto);
            saveFormData(application, dto);
        } else {
            validate(dto, ValidationGroupApplyMandatoryApplication.class);
            List<EntityConstraintViolation> acceptedViolations = validateWithConfirm(dto, acceptedPathViolations, ValidationGroupApplyOptionalApplication.class);
            dto.setRejectionReason(getRejectionReasonText(acceptedViolations));

            application = updateApplication(dto);
            updateBasicData(application, dto);
            saveReportData(application, dto);
            updateFormData(application, dto);
        }
        return application;
    }

    public GrantApplication execute(Long id, T dto, boolean checkVatRegNumDup){

        //STATUS
        GrantApplicationStatus status = applicationStatusRepository.get(GrantApplicationStatus.EXECUTED_CODE);
        dto.setStatus(DictionaryDTO.create(status));
        dto.setRejectionReason(null);

        validate(dto, ValidationGroupExecuteApplication.class);
        GrantApplication grantApplication = updateApplication(dto);
        updateBasicData(grantApplication, dto);
        updateReportData(grantApplication, dto);
        saveOrUpdateEnterprise(grantApplication, dto, checkVatRegNumDup);
        updateFormData(grantApplication, dto);

        orderService.createOrder(grantApplication);

        return grantApplication;
    }

    public GrantApplication reject(Long id, T dto){

        //STATUS
        GrantApplicationStatus status = applicationStatusRepository.get(GrantApplicationStatus.REJECTED_CODE);
        dto.setStatus(DictionaryDTO.create(status));
        dto.setConsiderationDate(new Date());

        validate(dto, ValidationGroupRejectApplication.class);
        GrantApplication grantApplication = updateApplication(dto);
        updateBasicData(grantApplication, dto);
        updateReportData(grantApplication, dto);
        updateFormData(grantApplication, dto);

        //WYSLANIE MAILA
        String emailsTo = findEmailAddressToSend(grantApplication);
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("rejectionReason", grantApplication.getRejectionReason())
                                                                    .add("grantProgramName", grantApplication.getProgram().getProgramName());
        sendPublicGrantProgramEmail(grantApplication, EmailTemplate.GA_REJECT, mailPlaceholders, emailsTo, null);

        return grantApplication;
    }

    @Override
    public void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId,
                                            MailPlaceholders mailPlaceholders, String addressesTo,
                                            List<MailAttachmentDTO> attachments){

        //ADRESY
        String emailFrom = applicationParametersService.getGryfPbeDefPubEmailFrom();
        emailFrom = findGrantProgramParam(grantApplication.getProgram(), GrantProgramParam.EMAIL_FROM, emailFrom);
        String emailsReplyTo = applicationParametersService.getGryfPbeDefPubEmailReplyTo();
        emailsReplyTo = findGrantProgramParam(grantApplication.getProgram(), GrantProgramParam.EMAIL_REPLAY_TO, emailsReplyTo);

        mailService.scheduleTemplateMail(emailTemplateId, mailPlaceholders,
                                        emailFrom, emailsReplyTo, addressesTo, null,
                                        EmailSourceType.GRANT_APP, grantApplication.getId(), attachments);
    }

    @Override
    public void sendPublicGrantProgramEmail(GrantApplication grantApplication, String emailTemplateId,
                                            String subject, String body, String emailsTo,
                                            List<MailAttachmentDTO> attachments){
        //ADRESY
        String emailFrom = applicationParametersService.getGryfPbeDefPubEmailFrom();
        emailFrom = findGrantProgramParam(grantApplication.getProgram(), GrantProgramParam.EMAIL_FROM, emailFrom);
        String emailsReplyTo = applicationParametersService.getGryfPbeDefPubEmailReplyTo();
        emailsReplyTo = findGrantProgramParam(grantApplication.getProgram(), GrantProgramParam.EMAIL_REPLAY_TO, emailsReplyTo);

        mailService.scheduleMail(emailTemplateId, subject, body,
                                emailFrom, emailsReplyTo, emailsTo, null,
                                EmailSourceType.GRANT_APP, grantApplication.getId(), attachments);
    }

    //PROTECTED METHODS - VALIDATE

    protected List<EntityConstraintViolation> generateViolation(T dto, Class ... classes) {
        List<EntityConstraintViolation> violations = validateService.generateViolation(dto, classes);

        //ATTACHEMENT VALIDATION
        if(GryfUtils.isAssignableFrom(ValidationGroupNewGrantApplication.class, classes)) {
            validateService.addFileMaxSizePrivilege(violations, "attachments", dto.getAttachments());
        }

        //APPLICED VALIDATION
        if(GryfUtils.isAssignableFrom(ValidationGroupApplyOptionalApplication.class, classes)) {
            addViolationRequiredAttachment(dto, violations);
        }

        //REJECT VALIDATION
        if(GryfUtils.isAssignableFrom(ValidationGroupRejectApplication.class, classes)) {
            addViolationRequiredEmails(dto, violations);
        }

        return violations;
    }

    protected void addViolationRequiredAttachment(T dto, List<EntityConstraintViolation> violations){
        if(dto.getApplicationVersion() != null) {
            GrantApplicationVersion applicationVersion = applicationVersionRepository.get((Long) dto.getApplicationVersion().getId());

            for (GrantApplicationAttachmentRequired requiredAttachment : applicationVersion.getAttachmentRequiredList()) {
                boolean isAttachmentInDTO = false;
                GrantApplicationAttachmentDTO[] attachmentDtoTab = dto.getAttachments() != null ? dto.getAttachments().toArray(
                        new GrantApplicationAttachmentDTO[dto.getAttachments().size()]) : new GrantApplicationAttachmentDTO[0];
                for (int i = 0; i < attachmentDtoTab.length; i++) {

                    //ATTACHMENT ISTNIEJE
                    if (requiredAttachment.getId().getName().equals(attachmentDtoTab[i].getName())) {

                        //NE DODANO PLIKU
                        if (!attachmentDtoTab[i].getFileIncluded() && StringUtils.isEmpty(attachmentDtoTab[i].getOriginalFileName())) {
                            violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME + "[" + i + "].file",
                                    String.format("Nie dodano pliku do wymaganego załacznika o nazwie '%s'", requiredAttachment.getId().getName()), null));
                        }
                        isAttachmentInDTO = true;
                        break;
                    }
                }

                //ATTACMENT NIE ISTIEJE
                if (!isAttachmentInDTO) {
                    violations.add(new EntityConstraintViolation(ReimbursementDTO.REIMBURSEMENT_ATTACHMENTS_ATTR_NAME,
                            String.format("Nie dodano wymaganego załacznika o nazwie '%s'", requiredAttachment.getId().getName()), null));
                }
            }
        }
    }

    protected void addViolationRequiredEmails(T dto, List<EntityConstraintViolation> violations){
        boolean flag = false;
        for (GrantApplicationContactDataDTO contact : dto.getContacts()) {
            if (!StringUtils.isEmpty(contact.getEmail())) {
                flag = true;
            }
        }
        EnterpriseSearchResultDTO enterpriseDTO = dto.getEnterprise();
        if(!flag && enterpriseDTO != null){
            Enterprise enterprise = enterpriseRepository.get(enterpriseDTO.getId());
            for (EnterpriseContact contact : enterprise.getContacts()) {
                ContactType contactType = contact.getContactType();
                if (ContactType.TYPE_EMAIL.equals(contactType.getType())) {
                    if (!StringUtils.isEmpty(contact.getContactData())) {
                        flag = true;
                    }
                }
            }
        }
        if(!flag){
            violations.add(new EntityConstraintViolation("contacts", "Należy dodać na wniosku kontakt z adresem amail lub dodać " +
                    "przedsiębiorstwo które posiada kontakty typu emial", null));
        }
    }

    protected void validate(T dto, Class ... classes) {
        List<EntityConstraintViolation> violations = generateViolation(dto, classes);
        validateService.validate(violations);
    }

    protected List<EntityConstraintViolation> validateWithConfirm(T dto, List<String> acceptedPathViolations, Class ... classes) {
        List<EntityConstraintViolation> violations = generateViolation(dto, classes);

        List<EntityConstraintViolation> violationsInPath = new ArrayList<>();
        List<EntityConstraintViolation> violationsOutPath = new ArrayList<>();
        validateService.classifyByPath(violations, acceptedPathViolations, violationsInPath, violationsOutPath);

        validateService.validateWithConfirm(violationsOutPath);
        return violationsInPath;
    }

    //PROTECTED METHODS - SAVE

    protected GrantApplication saveApplication(T dto){
        GrantApplication application = new GrantApplication();
        application.setStatus(applicationStatusRepository.get(GrantApplicationStatus.NEW_CODE));

        application = applicationRepository.save(application);

        fillApplication(application, dto);


        dto.setId(application.getId());
        return application;
    }

    protected GrantApplicationBasicData saveBasicData(GrantApplication application, T dto){
        GrantApplicationBasicData applicationBasicData = new GrantApplicationBasicData();
        application.setBasicData(applicationBasicData);
        applicationBasicData.setApplication(application);
        applicationBasicData.setId(application.getId());

        return fillApplicationBasicData(application.getBasicData(), dto);
    }

    protected void saveFormData(GrantApplication application, T dto){
        GrantApplicationFormData applicationFormData = new GrantApplicationFormData();
        application.setFormData(applicationFormData);
        applicationFormData.setApplication(application);
        applicationFormData.setId(application.getId());

        fillApplicationFormData(application.getFormData(), dto);
    }

    protected void saveReportData(GrantApplication application, T dto) {
        GrantApplicationRepHeader repHeader = applicationRepHeaderRepository.get(application.getApplicationVersion().getId());
        if(repHeader != null){
            GrantApplicationRepData repData = new GrantApplicationRepData();
            repData.setApplication(application);
            repData.setHeader(repHeader);
            repData.setId(application.getId());
            application.setReportData(repData);
            fillApplicationReportData(repData, dto);
        }
    }

    //PROTECTED METHODS - UPDATE

    protected GrantApplication updateApplication(T dto) {
        GrantApplication application = applicationRepository.get(dto.getId());

        if(!Objects.equals(application.getVersion(), dto.getVersion())){
            throw new StaleDataException(application.getId(), application);
        }

        fillApplication(application, dto);
        return applicationRepository.update(application, application.getId());
    }

    protected GrantApplicationBasicData updateBasicData(GrantApplication application, T dto) {
        return fillApplicationBasicData(application.getBasicData(), dto);
    }

    protected void updateFormData(GrantApplication application, T dto){
        fillApplicationFormData(application.getFormData(), dto);
    }

    protected void updateReportData(GrantApplication application, T dto) {
        GrantApplicationRepData repData = application.getReportData();
        if(repData != null){
            fillApplicationReportData(repData, dto);
        }
    }

    //VALIDATE STATUSES

    protected void validateStatuses(GrantApplicationStatus statusSrc, GrantApplicationStatus statusDes){
        if(!Objects.equals(statusSrc, statusDes)) {
            Set<String> allowedStatuses = statusMap.get(statusSrc.getId());
            if (!allowedStatuses.contains(statusDes.getId())) {
                String message = (statusSrc == null) ? String.format("Nie mozna stworzyć nowego wniosku w statusie '%s'", statusDes.getName()) :
                        String.format("Wniosek jest w statusie '%s' - z tego statusu nie można przejść do statusu '%s'", statusSrc.getName(), statusDes.getName());
                validateService.validate(message);
            }
        }
    }

    //SAVE ENTERPRISE FROM APPLICATION

    protected Enterprise saveOrUpdateEnterprise(GrantApplication grantApplication, T dto, boolean checkVatRegNumDup){
        if (grantApplication.getEnterprise() == null) {
            saveEnterprise(grantApplication, dto, checkVatRegNumDup);
        } else {
            updateEnterprise(grantApplication, dto, checkVatRegNumDup);
        }
        return grantApplication.getEnterprise();
    }

    protected Enterprise saveEnterprise(GrantApplication grantApplication, T dto, boolean checkVatRegNumDup){
        Enterprise enterprise = null;
        if(dto.getEnterprise() == null) {

            enterprise = new Enterprise();
            enterprise.setName(dto.getEnterpriseName());
            enterprise.setVatRegNum(dto.getVatRegNum());
            enterprise.setAddressInvoice(dto.getAddressInvoice());
            enterprise.setZipCodeInvoice((dto.getZipCodeInvoice() != null) ? zipCodeRepository.get(dto.getZipCodeInvoice().getId()) : null);
            enterprise.setAddressCorr(dto.getAddressCorr());
            enterprise.setZipCodeCorr((dto.getZipCodeCorr() != null) ? zipCodeRepository.get(dto.getZipCodeCorr().getId()) : null);
            enterprise.setAccountRepayment(dto.getAccountRepayment());
            enterprise.setRemarks("Utworzone z wniosku" + grantApplication.getId());
            saveEnterpriseContacts(enterprise, dto);

            try {
                enterprise = enterpriseService.saveEnterprise(enterprise, checkVatRegNumDup);
            } catch (VatRegNumTrainingInstitutionExistException e) {
                GryfUtils.rethrowException(e, "Wystapił błąd przy zapisie danych do MŚP: ");
            } catch (EntityValidationException e) {
                GryfUtils.rethrowException(e, "Wystapił błąd przy zapisie danych do MŚP: ");
            }

            grantApplication.setEnterprise(enterprise);
            dto.setEnterprise(EnterpriseSearchResultDTO.create(enterprise));
        }
        return enterprise;
    }

    protected void updateEnterprise(GrantApplication grantApplication, T dto, boolean checkVatRegNumDup){
        Enterprise enterprise = grantApplication.getEnterprise();

        if(enterprise.getVatRegNum().equals(dto.getVatRegNum())){
            checkVatRegNumDup = false;
        }

        enterprise.setName(dto.getEnterpriseName());
        enterprise.setVatRegNum(dto.getVatRegNum());
        enterprise.setAddressInvoice(dto.getAddressInvoice());
        enterprise.setZipCodeInvoice((dto.getZipCodeInvoice() != null) ? zipCodeRepository.get(dto.getZipCodeInvoice().getId()) : null);
        enterprise.setAddressCorr(dto.getAddressCorr());
        enterprise.setZipCodeCorr((dto.getZipCodeCorr() != null) ? zipCodeRepository.get(dto.getZipCodeCorr().getId()) : null);
        enterprise.setAccountRepayment(dto.getAccountRepayment());
        updateEnterpriseContacts(enterprise, dto);

        try {
            enterpriseService.updateEnterprise(enterprise, checkVatRegNumDup);
        }catch (VatRegNumTrainingInstitutionExistException e){
            GryfUtils.rethrowException(e, "Wystapił błąd przy zapisie danych do MŚP: ");
        }catch(EntityValidationException e){
            GryfUtils.rethrowException(e, "Wystapił błąd przy zapisie danych do MŚP: ");
        }

        grantApplication.setEnterprise(enterprise);
        dto.setEnterprise(EnterpriseSearchResultDTO.create(enterprise));
    }

    protected void saveEnterpriseContacts(Enterprise enterprise, T dto){
        for (GrantApplicationContactDataDTO contactDTO : dto.getContacts()) {
            if(!StringUtils.isEmpty(contactDTO.getEmail())){
                addContact(enterprise, ContactType.TYPE_EMAIL, contactDTO.getEmail(), contactDTO.getName(), dto.getId());
            }
            if(!StringUtils.isEmpty(contactDTO.getPhone())){
                addContact(enterprise, ContactType.TYPE_PHONE, contactDTO.getPhone(), contactDTO.getName(), dto.getId());
            }
        }
    }

    protected void updateEnterpriseContacts(Enterprise enterprise, T dto){
        for (final GrantApplicationContactDataDTO contactApplicationDTO : dto.getContacts()) {
            if (!StringUtils.isEmpty(contactApplicationDTO.getEmail())) {
                if (!containContact(enterprise.getContacts(), ContactType.TYPE_EMAIL, contactApplicationDTO.getEmail())) {
                    addContact(enterprise, ContactType.TYPE_EMAIL, contactApplicationDTO.getEmail(), contactApplicationDTO.getName(), dto.getId());
                }
            }
            if (!StringUtils.isEmpty(contactApplicationDTO.getPhone())) {
                if (!containContact(enterprise.getContacts(), ContactType.TYPE_PHONE, contactApplicationDTO.getPhone())) {
                    addContact(enterprise, ContactType.TYPE_PHONE, contactApplicationDTO.getPhone(), contactApplicationDTO.getName(), dto.getId());
                }
            }
        }
    }

    protected boolean containContact(List<EnterpriseContact> contacts, final String contactTypeType, final String contactDataDTO){
        return GryfUtils.contain(contacts, new GryfUtils.Predicate<EnterpriseContact>() {
            public boolean apply(EnterpriseContact input) {

                ContactType contactType = input.getContactType();
                if (contactType != null && contactTypeType.equals(contactType.getType())) {
                    if (!StringUtils.isEmpty(contactDataDTO) && !StringUtils.isEmpty(input.getContactData())) {

                        String tempContactDataDTO = contactDataDTO.trim().toUpperCase();
                        String tempContactData = input.getContactData().trim().toUpperCase();
                        if (tempContactDataDTO.equals(tempContactData)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    protected void addContact(Enterprise enterprise, String contactTypeType, String contactData, String name, Long id){
        EnterpriseContact contact = new EnterpriseContact();
        contact.setContactType(contactTypeRepository.get(contactTypeType));
        contact.setContactData(contactData);
        contact.setRemarks(String.format("%s z wniosku %s", name, id));
        enterprise.addContact(contact);
    }

    //PROTECTED METHODS - FILL ENTITY

    protected GrantApplication fillApplication(GrantApplication application, T dto){

        application.setRejectionReason(dto.getRejectionReason());
        application.setReceiptDate(dto.getReceiptDate());

        //VERSION
        GrantApplicationVersion applicationVersion = dto.getApplicationVersion() == null ? null :
                                                    applicationVersionRepository.get((Long) dto.getApplicationVersion().getId());
        application.setApplicationVersion(applicationVersion);

        //PROGRAM
        GrantProgram program = dto.getGrantProgram() == null ? null :
                grantProgramRepository.get((Long) dto.getGrantProgram().getId());
        application.setProgram(program);

        //ENTERPRISE
        Enterprise enterprise = dto.getEnterprise() == null ? null : enterpriseRepository.get(dto.getEnterprise().getId());
        application.setEnterprise(enterprise);

        GrantApplicationStatus status = applicationStatusRepository.get((String) dto.getStatus().getId());
        validateStatuses(application.getStatus(), status);
        application.setStatus(status);
        application.setApplyDate(dto.getApplyDate());
        application.setConsiderationDate(dto.getConsiderationDate());

        //ATTACHMENT
        organizeAttachment(application, dto.getAttachments());

        //MODIFICATION DATA (JEZELI NIE BYLO ZMIAN -> TO I TAK PODBIJE WERSJE)
        //@OptimisticLocking NIE DZIAŁA DLA OneToOne
        application.setModifiedTimestamp(new Date());

        return application;
    }

    protected GrantApplicationBasicData fillApplicationBasicData(GrantApplicationBasicData applicationBasicData, T dto){

        //ENTERPRISE DATA
        applicationBasicData.setEnterpriseName(dto.getEnterpriseName());
        applicationBasicData.setVatRegNum(dto.getVatRegNum());

        //ENTITY TYPE
        EnterpriseEntityType enterpriseEntityType = dto.getEntityType() == null ? null:
                                            enterpriseEntityTypeRepository.get((String) dto.getEntityType().getId());
        applicationBasicData.setEntityType(enterpriseEntityType);

        //ADDRESS INVOICE
        applicationBasicData.setAddressInvoice(dto.getAddressInvoice());
        ZipCodeSearchResultDTO zipCodeInvoiceDTO = dto.getZipCodeInvoice();
        if(zipCodeInvoiceDTO != null){
            ZipCode zipCode = zipCodeRepository.get(zipCodeInvoiceDTO.getId());
            applicationBasicData.setZipCodeInvoice(zipCode);
        }

        applicationBasicData.setCounty(dto.getCounty());

        //ENTERPRISE SIZE
        if(dto.getEnterpriseSize() != null){
            EnterpriseSize enterpriseSize = enterpriseSizeRepository.get((String) dto.getEnterpriseSize().getId());
            applicationBasicData.setEnterpriseSize(enterpriseSize);
        }

        applicationBasicData.setVouchersNumber(dto.getVouchersNumber());

        //ADDRESS CORR
        if(dto.isCorrBothFill()){
            applicationBasicData.setAddressCorr(dto.getAddressCorr());
            ZipCodeSearchResultDTO zipCodeCorrDTO = dto.getZipCodeCorr();
            if(zipCodeCorrDTO != null){
                ZipCode zipCode = zipCodeRepository.get(zipCodeCorrDTO.getId());
                applicationBasicData.setZipCodeCorr(zipCode);
            }
        }else if(dto.isCorrBothEmpty()){
            applicationBasicData.setAddressCorr(applicationBasicData.getAddressInvoice());
            applicationBasicData.setZipCodeCorr(applicationBasicData.getZipCodeInvoice());
            dto.setAddressCorr(dto.getAddressInvoice());
            dto.setZipCodeCorr(dto.getZipCodeInvoice());
        }

        applicationBasicData.setAccountRepayment(dto.getAccountRepayment());

        //PKD
        organizePkdList(applicationBasicData, dto.getPkdList());

        //CONSTACT
        organizeContacts(applicationBasicData, dto.getContacts(), dto.getDeliveryContacts());

        return applicationBasicData;
    }

    protected abstract GrantApplicationRepData fillApplicationReportData(GrantApplicationRepData repData, T dto);

    protected GrantApplicationFormData fillApplicationFormData(GrantApplicationFormData applicationFormData, T dto) {

        dto.setVersion((dto.getVersion() != null) ? dto.getVersion() + 1 : 1);
        String dtoValue = GrantApplicationParser.writeApplicationData(dto);
        applicationFormData.setFormData(dtoValue);
        return applicationFormData;
    }

    //PROTECTED METHODS - FILL DTO
    
    /**
     * Niektóre pola w DTO wniosku przekazywanym do frontu są aktualizowane na podstawie głównej tabeli wniosku (po to by można było je zmieniać "od strony bazy danych" ).
     * 
     * @param dto - dto zaczytane z tabeli FORM_DATA, w którym należy podmienić podstawowe parametry wniosku na te z głównej tabeli wniosku
     * @param application obiekt wniosku z głównej tabeli wniosku
     * @return dto uzupełnione o aktualne wartości kluczowych pól pobranych z głownej tabeli wniosków
     */
    protected T fillApplicationDto(T dto, GrantApplication application){
        dto.setStatus(DictionaryDTO.create(application.getStatus()));
        dto.setApplicationVersion(GrantApplicationVersionDictionaryDTO.create(application.getApplicationVersion()));
        dto.setGrantProgram(DictionaryDTO.create(application.getProgram()));
        dto.setEnterprise(EnterpriseSearchResultDTO.create(application.getEnterprise()));
        dto.setApplyDate(application.getApplyDate());
        dto.setConsiderationDate(application.getConsiderationDate());
        dto.setRejectionReason(application.getRejectionReason());
        dto.setReceiptDate(application.getReceiptDate());
        dto.setVersion(application.getVersion());
        for (GrantApplicationAttachmentDTO attachmentDTO : dto.getAttachments()) {
            attachmentDTO.setMandatory(isAttachmentRequired(application.getApplicationVersion(), attachmentDTO.getName()));
        }
        return dto;
    }

    //PROTECTED METHODS - ORGANIZE LIST (CONTACTS)

    protected void organizeContacts(GrantApplicationBasicData applicationBasicData, List<GrantApplicationContactDataDTO> contactDtoList, List<GrantApplicationContactDataDTO> deliveryContactDtoList){

        //CONTACTS
        if(contactDtoList == null) {
            contactDtoList = new ArrayList<>();
        }
        List<GrantApplicationContactData> contacts = GryfUtils.filter(applicationBasicData.getContacts(), new GryfUtils.Predicate<GrantApplicationContactData>() {
            public boolean apply(GrantApplicationContactData input) {
                return GrantApplicationContactType.CONTACT == input.getContactType();
            }
        });
        organizeContactList(applicationBasicData, contacts,  contactDtoList, GrantApplicationContactType.CONTACT);

        //DELIVERY CONTACTS
        if(deliveryContactDtoList == null) {
            deliveryContactDtoList = new ArrayList<>();
        }
        List<GrantApplicationContactData> deliveryContacts = GryfUtils.filter(applicationBasicData.getContacts(), new GryfUtils.Predicate<GrantApplicationContactData>() {
            public boolean apply(GrantApplicationContactData input) {
                return GrantApplicationContactType.DELIVERY == input.getContactType();
            }
        });
        organizeContactList(applicationBasicData, deliveryContacts,  deliveryContactDtoList, GrantApplicationContactType.DELIVERY);
    }

    protected void organizeContactList(GrantApplicationBasicData applicationBasicData, List<GrantApplicationContactData> contacts,  List<GrantApplicationContactDataDTO> contactDtoList, GrantApplicationContactType contactType){
        if(contactDtoList == null){
            contactDtoList = new ArrayList<>();
        }
        List<ContactTempDTO> contactTempDtoList = makeContactTempDtoList(contacts, contactDtoList);

        for (ContactTempDTO contactTempDTO : contactTempDtoList) {
            GrantApplicationContactData contact = contactTempDTO.getContact();
            GrantApplicationContactDataDTO contactDTO = contactTempDTO.getContactDTO();

            //UPDATE OLD RECORD
            if(contact != null && contactDTO != null){
                contact.setContactType(contactType);
                contact.setName(contactDTO.getName());
                contact.setEmail(contactDTO.getEmail());
                contact.setPhone(contactDTO.getPhone());

                //REMOVE OLD RECORD
            }else if(contact != null){
                applicationBasicData.removeContact(contact);

                //ADD NEW RECORD
            }else if(contactDTO != null){
                contact = new GrantApplicationContactData();
                contact.setContactType(contactType);
                contact.setName(contactDTO.getName());
                contact.setEmail(contactDTO.getEmail());
                contact.setPhone(contactDTO.getPhone());
                contact = applicationContactDataRepository.save(contact);
                applicationBasicData.addContact(contact);

                contactDTO.setId(contact.getId());
            }
        }
    }

    protected List<ContactTempDTO> makeContactTempDtoList(List<GrantApplicationContactData> contacts, List<GrantApplicationContactDataDTO> contactDtoList) {
        List<ContactTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, GrantApplicationContactData> contactMap = GryfUtils.constructMap(contacts, new GryfUtils.MapConstructor<Long, GrantApplicationContactData>() {
            public boolean isAddToMap(GrantApplicationContactData input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationContactData input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, GrantApplicationContactDataDTO> contactDtoMap = GryfUtils.constructMap(contactDtoList, new GryfUtils.MapConstructor<Long, GrantApplicationContactDataDTO>() {
            public boolean isAddToMap(GrantApplicationContactDataDTO input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationContactDataDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (GrantApplicationContactDataDTO contactDTO : contactDtoList) {
            ContactTempDTO temp = new ContactTempDTO();
            temp.setContactDTO(contactDTO);
            temp.setContact((contactDTO.getId() != null) ? contactMap.get(contactDTO.getId()) : null);
            result.add(temp);
        }
        for (GrantApplicationContactData contact : contacts) {
            if(!contactDtoMap.containsKey(contact.getId())){
                ContactTempDTO temp = new ContactTempDTO();
                temp.setContact(contact);
                result.add(temp);
            }
        }

        return result;
    }

    protected class ContactTempDTO{

        private GrantApplicationContactData contact;

        private GrantApplicationContactDataDTO contactDTO;

        public GrantApplicationContactData getContact() {
            return contact;
        }

        public void setContact(GrantApplicationContactData contact) {
            this.contact = contact;
        }

        public GrantApplicationContactDataDTO getContactDTO() {
            return contactDTO;
        }

        public void setContactDTO(GrantApplicationContactDataDTO contactDTO) {
            this.contactDTO = contactDTO;
        }

    }

    //PROTECTED METHODS - ORGANIZE LIST (PKD)

    protected void organizePkdList(GrantApplicationBasicData applicationBasicData,  List<GrantApplicationPkdDTO> pkdDtoList){
        if(pkdDtoList == null){
            pkdDtoList = new ArrayList<>();
        }
        List<PkdTempDTO> pkdTempDtoList = makePkdTempDtoList(applicationBasicData.getPkdList(), pkdDtoList);

        for (PkdTempDTO pkdTempDTO : pkdTempDtoList) {
            GrantApplicationPkd pkd = pkdTempDTO.getPkd();
            GrantApplicationPkdDTO pkdDTO = pkdTempDTO.getPkdDTO();

            //UPDATE OLD RECORD
            if(pkd != null && pkdDTO != null){
                pkd.setPkdCode(pkdDTO.getName());

            //REMOVE OLD RECORD
            }else if(pkd != null){
                applicationBasicData.removePkd(pkd);

            //ADD NEW RECORD
            }else if(pkdDTO != null){
                pkd = new GrantApplicationPkd();
                pkd.setPkdCode(pkdDTO.getName());
                pkd = applicationPkdRepository.save(pkd);
                applicationBasicData.addPkd(pkd);

                pkdDTO.setId(pkd.getId());
            }
        }
    }

    protected List<PkdTempDTO> makePkdTempDtoList(List<GrantApplicationPkd> pkds, List<GrantApplicationPkdDTO> pkdDtoList) {
        List<PkdTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, GrantApplicationPkd> pkdMap = GryfUtils.constructMap(pkds, new GryfUtils.MapConstructor<Long, GrantApplicationPkd>() {
            public boolean isAddToMap(GrantApplicationPkd input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationPkd input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, GrantApplicationPkdDTO> pkdDtoMap = GryfUtils.constructMap(pkdDtoList, new GryfUtils.MapConstructor<Long, GrantApplicationPkdDTO>() {
            public boolean isAddToMap(GrantApplicationPkdDTO input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationPkdDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (GrantApplicationPkdDTO pkdDTO : pkdDtoList) {
            PkdTempDTO temp = new PkdTempDTO();
            temp.setPkdDTO(pkdDTO);
            temp.setPkd((pkdDTO.getId() != null) ? pkdMap.get(pkdDTO.getId()) : null);
            result.add(temp);
        }
        for (GrantApplicationPkd pkd : pkds) {
            if(!pkdDtoMap.containsKey(pkd.getId())){
                PkdTempDTO temp = new PkdTempDTO();
                temp.setPkd(pkd);
                result.add(temp);
            }
        }

        return result;
    }

    protected class PkdTempDTO{

        private GrantApplicationPkd pkd;

        private GrantApplicationPkdDTO pkdDTO;

        public GrantApplicationPkd getPkd() {
            return pkd;
        }

        public void setPkd(GrantApplicationPkd pkd) {
            this.pkd = pkd;
        }

        public GrantApplicationPkdDTO getPkdDTO() {
            return pkdDTO;
        }

        public void setPkdDTO(GrantApplicationPkdDTO pkdDTO) {
            this.pkdDTO = pkdDTO;
        }

    }

    //PROTECTED METHODS - ORGANIZE LIST (ATTACHMENTS)

    protected void organizeAttachment(GrantApplication application, List<GrantApplicationAttachmentDTO> attachmentDtoList) {
        if(attachmentDtoList == null){
            attachmentDtoList = new ArrayList<>();
        }
        List<AttachmentTempDTO> attachmentTempDtoList = makeAttachmentTempDtoList(application.getAttachments(), attachmentDtoList);

        for (AttachmentTempDTO attachmentTempDTO : attachmentTempDtoList) {
            GrantApplicationAttachment attachment = attachmentTempDTO.getAttachment();
            GrantApplicationAttachmentDTO attachmentDTO = attachmentTempDTO.getAttachmentDTO();

            //UPDATE OLD RECORD
            if(attachment != null && attachmentDTO != null){
                attachment.setName(attachmentDTO.getName());
                attachment.setRemarks(attachmentDTO.getRemarks());
                if(attachmentDTO.getFile() != null){
                    FileDTO fileDTO = attachmentDTO.getFile();

                    String fileName = findAttachmentName(application, attachment);
                    String fileLocation = fileService.writeFile(FileType.GRANT_APPLICATIONS, fileName, fileDTO, attachment);
                    if(attachment.getFileLocation() != null && !fileLocation.equals(attachment.getFileLocation())){
                        fileService.deleteFile(attachment.getFileLocation());
                    }
                    attachment.setFileLocation(fileLocation);
                    attachment.setOriginalFileName(fileDTO.getOriginalFilename());
                    attachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
                    attachmentDTO.setFileIncluded(false);
                }

            //REMOVE OLD RECORD
            }else if(attachment != null){
                application.removeAttachment(attachment);
                fileService.deleteFile(attachment.getFileLocation());

            //ADD NEW RECORD
            }else if(attachmentDTO != null){
                attachment = new GrantApplicationAttachment();
                attachment.setName(attachmentDTO.getName());
                attachment.setRemarks(attachmentDTO.getRemarks());
                attachment = applicationAttachmentRepository.save(attachment);
                application.addAttachment(attachment);

                if(attachmentDTO.getFile() != null){
                    FileDTO fileDTO = attachmentDTO.getFile();

                    String fileName = findAttachmentName(application, attachment);
                    String fileLocation = fileService.writeFile(FileType.GRANT_APPLICATIONS, fileName, fileDTO, attachment);
                    attachment.setFileLocation(fileLocation);
                    attachment.setOriginalFileName(fileDTO.getOriginalFilename());
                    attachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
                    attachmentDTO.setFileIncluded(false);
                }
                attachmentDTO.setId(attachment.getId());
            }
        }
    }

    protected List<AttachmentTempDTO> makeAttachmentTempDtoList(List<GrantApplicationAttachment> attachments, List<GrantApplicationAttachmentDTO> attachmentDtoList) {
        List<AttachmentTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, GrantApplicationAttachment> attachmentMap = GryfUtils.constructMap(attachments, new GryfUtils.MapConstructor<Long, GrantApplicationAttachment>() {
            public boolean isAddToMap(GrantApplicationAttachment input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationAttachment input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, GrantApplicationAttachmentDTO> attachmentDtoMap = GryfUtils.constructMap(attachmentDtoList, new GryfUtils.MapConstructor<Long, GrantApplicationAttachmentDTO>() {
            public boolean isAddToMap(GrantApplicationAttachmentDTO input) {
                return input.getId() != null;
            }
            public Long getKey(GrantApplicationAttachmentDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (GrantApplicationAttachmentDTO attachmentDTO : attachmentDtoList) {
            AttachmentTempDTO temp = new AttachmentTempDTO();
            temp.setAttachmentDTO(attachmentDTO);
            temp.setAttachment((attachmentDTO.getId() != null) ? attachmentMap.get(attachmentDTO.getId()) : null);
            result.add(temp);
        }
        for (GrantApplicationAttachment attachment : attachments) {
            if(!attachmentDtoMap.containsKey(attachment.getId())){
                AttachmentTempDTO temp = new AttachmentTempDTO();
                temp.setAttachment(attachment);
                result.add(temp);
            }
        }

        return result;
    }

    protected String findAttachmentName(GrantApplication application, GrantApplicationAttachment attachment){
        String fileName = String.format("%s_%s_%s", application.getId(), attachment.getName(), attachment.getId());
        return StringUtils.convertFileName(fileName);
    }

    protected class AttachmentTempDTO{

        private GrantApplicationAttachment attachment;

        private GrantApplicationAttachmentDTO attachmentDTO;

        public GrantApplicationAttachment getAttachment() {
            return attachment;
        }

        public void setAttachment(GrantApplicationAttachment attachment) {
            this.attachment = attachment;
        }

        public GrantApplicationAttachmentDTO getAttachmentDTO() {
            return attachmentDTO;
        }

        public void setAttachmentDTO(GrantApplicationAttachmentDTO attachmentDTO) {
            this.attachmentDTO = attachmentDTO;
        }

    }

    //PROTECTED METHODS - OTHERS

    protected String getRejectionReasonText(List<EntityConstraintViolation> violations) {
        String rejectionReason = "";
        for (EntityConstraintViolation v : violations) {
            int indexBeforeAdd = rejectionReason.length();
            rejectionReason += v.getMessage() + ",\n";
            if (rejectionReason.length() > REJECTION_REASON_LENGTH_LIMIT) {
                rejectionReason = rejectionReason.substring(0, indexBeforeAdd);
                rejectionReason += "i inne...";
                break;
            }
        }
        return rejectionReason;
    }

    /**
     * Wyszukije parametr dla konkretnego programu. Jezeli nie znajdzie zadnego to zwraca domyslna wartosc parametru. Gdy znajdzie wiecej niz jeden zwraca blad.
     * @param grantProgram progrma dofinanoswania
     * @param paramTypeId id parametru
     * @param defaultValue wartosc domyslna
     * @return znaleziona wartosc lub wartosc domyslna
     */
    protected String findGrantProgramParam(GrantProgram grantProgram, String paramTypeId, String defaultValue){
        List<GrantProgramParam> params = grantProgramParamRepository.findByGrantProgramInDate(grantProgram.getId(), paramTypeId, new Date());
        if(params.size() == 0){
            return defaultValue;
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Dla danego programu dofinanoswania [%s] znalziono wiecej niże jeden parametr [%s] w danej dacie",
                    grantProgram.getId(), paramTypeId));
        }
        return params.get(0).getValue();
    }

    /**
     * Zwraca emaile do których powinnien być wysłany mail (kontakty z wniosku oraz z MSP)
     * @param grantApplication skladany wniosek
     * @return emaile oddzielone przecinkiem
     */
    protected String findEmailAddressToSend(GrantApplication grantApplication) {
        Set<String> set = grantApplicationsService.getEmailRecipients(grantApplication, null);
        set = enterpriseService.getEmailRecipients(grantApplication.getEnterprise(), set);
        return GryfUtils.formatEmailRecipientsSet(set);
    }

    /**
     * Jeżeli dla konkretnego wniosku byłą by potrzeba zwrocenia innej sesji \
     * meilowwej należy nadpisac tą metodą zwracając odpowiednia sesję.
     * @return sesja meilowa
     */
    protected Session createMailSession() {
        return null;
    }

    /**
     * Sprawdza czy załacznik o danej nazwie jest wymaganym załacznikiem.
     * @param applicationVersion wersja wniosku
     * @param name nazwa załącznika
     * @return true gdy jest wymaganym załacznikiem, false w przeciwnym wypadku
     */
    protected boolean isAttachmentRequired(GrantApplicationVersion applicationVersion, String name){
        for (GrantApplicationAttachmentRequired attachmentRequired : applicationVersion.getAttachmentRequiredList()) {
            if(Objects.equals(attachmentRequired.getId().getName(), name)){
                return true;
            }
        }
        return false;
    }
}
