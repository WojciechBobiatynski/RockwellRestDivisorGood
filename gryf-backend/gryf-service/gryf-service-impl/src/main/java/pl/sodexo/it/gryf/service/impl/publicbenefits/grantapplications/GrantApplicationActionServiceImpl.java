package pl.sodexo.it.gryf.service.impl.publicbenefits.grantapplications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.parsers.GrantApplicationParser;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationVersionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationActionService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.grantapplications.detailsform.GrantApplicationVersionEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.grantapplications.searchform.GrantApplicationEntityMapper;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@Service
@Transactional
public class GrantApplicationActionServiceImpl implements GrantApplicationActionService {

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

    @Autowired
    private GrantApplicationEntityMapper grantApplicationEntityMapper;

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private GrantApplicationVersionEntityMapper grantApplicationVersionEntityMapper;

    //PUBLIC METHODS

    @Override
    public List<GrantApplicationSearchResultDTO> findApplications(GrantApplicationSearchQueryDTO searchDTO) {
        List<GrantApplication> applications = applicationRepository.findApplications(searchDTO);
        return grantApplicationEntityMapper.convert(applications);
    }

    @Override
    public String findApplicationFormData(Long id) {
        GrantApplication application = applicationRepository.get(id);
        GrantApplicationVersion applicationVersion = application.getApplicationVersion();
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        return grantApplicationService.findActualApplicationFormData(application);
    }

    @Override
    public Long saveApplication(Long versionId, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.save(grantApplicationDTO);
        return application.getId();
    }

    @Override
    public Long updateApplication(Long versionId, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.update(grantApplicationDTO);
        return application.getId();
    }

    @Override
    public Long applyApplication(Long versionId, String data, List<FileDTO> fileDtoList, List<String> acceptedViolationsList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.get(versionId);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.apply(grantApplicationDTO, acceptedViolationsList);
        return application.getId();
    }

    @Override
    public Long executeApplication(Long id, String data, List<FileDTO> fileDtoList, boolean checkVatRegNumDup) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.findByApplication(id);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.execute(id, grantApplicationDTO, checkVatRegNumDup);
        return application.getId();
    }

    @Override
    public Long rejectApplication(Long id, String data, List<FileDTO> fileDtoList) {
        GrantApplicationVersion applicationVersion = applicationVersionRepository.findByApplication(id);
        GrantApplicationDTO grantApplicationDTO = GrantApplicationParser.readApplicationData(applicationVersion.getDtoClassName(), data, fileDtoList);
        GrantApplicationService grantApplicationService = BeanUtils.findGrantApplicationService(context, applicationVersion.getServiceBeanName());
        GrantApplication application = grantApplicationService.reject(id, grantApplicationDTO);
        return application.getId();
    }

    @Override
    public FileDTO getApplicationAttachmentFile(Long attachmentId) {
        GrantApplicationAttachment attachment = grantApplicationAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        manageLocking(id, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id, String message) {
        GrantApplication application = applicationRepository.get(id);
        throw new StaleDataException(application.getId(), application, message);
    }

    //DICTIONARIES

    @Override
    public List<DictionaryDTO> FindGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return dictionaryEntityMapper.convert(grantPrograms);
    }

    @Override
    public List<GrantApplicationVersionDictionaryDTO> findGrantApplicationVersionsDictionaries(Long grantProgramId) {
        List<GrantApplicationVersion> applicationVersions = applicationVersionRepository.findByProgram(grantProgramId, new Date());
        return grantApplicationVersionEntityMapper.convert(applicationVersions);
    }
}
