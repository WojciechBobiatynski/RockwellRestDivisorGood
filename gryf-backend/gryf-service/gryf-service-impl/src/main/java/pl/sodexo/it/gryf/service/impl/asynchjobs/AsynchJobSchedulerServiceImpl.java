package pl.sodexo.it.gryf.service.impl.asynchjobs;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobResultInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.searchform.AsynchJobSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.importdatarows.ImportDataRowSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.logging.NoLog;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.AsynchJobSearchDao;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.JobTypeRepository;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobStatus;
import pl.sodexo.it.gryf.model.asynch.JobType;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobFileService;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobSchedulerService;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;
import pl.sodexo.it.gryf.service.validation.asynchjobs.AsynchJobValidator;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service
@Transactional
public class AsynchJobSchedulerServiceImpl implements AsynchJobSchedulerService {

    //STATIC FIELDS

    private static final Logger LOGGER = LoggerFactory.getLogger(AsynchJobSchedulerServiceImpl.class);

    //PRIVATE FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Autowired
    private AsynchJobSearchDao asynchJobSearchDao;

    @Autowired
    private AsynchJobValidator asynchJobValidator;

    @Autowired
    private AsynchJobFileService asynchJobFileService;

    @Autowired
    private JobTypeRepository jobTypeRepository;

    private AsynchJobSchedulerService asynchJobSchedulerService;

    //LIFECYCLE METHODS

    @PostConstruct
    public void init(){
        asynchJobSchedulerService = context.getBean(AsynchJobSchedulerService.class);
    }

    //PUBLIC METHODS



    @Override
    public Long saveAsynchronizeJob(AsynchJobDetailsDTO createDTO){
        asynchJobValidator.validateJobProperties(createDTO);

        AsynchronizeJob job = new AsynchronizeJob();
        JobType jobType = jobTypeRepository.findByGrantProgramIdAndName(createDTO.getGrantProgramId(), createDTO.getType());
        job.setType(jobType.getName());
        job.setStatus(AsynchronizeJobStatus.N);
        job = asynchronizeJobRepository.save(job);

        createDTO.setId(job.getId());
        String path = asynchJobFileService.saveFile(createDTO);
        job.setParams(String.format("%s;%s", createDTO.getGrantProgramId(), path));

        asynchronizeJobRepository.save(job);

        return job.getId();
    }
    @Override
    @Scheduled(initialDelayString = "${gryf2.service.scheduler.asynchJobImport.initialDelay:15000}", fixedDelayString = "${gryf2.service.scheduler.asynchJobImport.fixedDelay:5000}")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @NoLog
    public void processAsynchronizeJob(){
        Long jobId;
        do{
            //RESERVE ONE JOB
            jobId = asynchJobSchedulerService.reserveAsynchronizeJob();

            if(jobId != null){
                try {

                    //ACTION JOB
                    LOGGER.info("Rozpoczęto procesowanie asynchronicznego joba, jobId={}", jobId);
                    AsynchronizeJobInfoDTO dto = asynchJobSchedulerService.getAsynchronizeJobInfoDTO(jobId);
                    AsynchJobService jobService = (AsynchJobService) BeanUtils.findBean(context, dto.getServiceName());
                    AsynchronizeJobResultInfoDTO resultDTO = jobService.processAsynchronizeJob(dto);

                    //SET SUCCESS STATUS
                    asynchJobSchedulerService.successEndJob(resultDTO);
                    LOGGER.info("Zakończono sukcesem procesowanie asynchronicznego joba, result={}",resultDTO);

                    //OBSLUGA BLEDOW
                }catch(EntityValidationException e){
                    LOGGER.error(String.format("Nieoczekiwane błąd biznesowy zadania asynchronicznego o numerze [%s] ", jobId), e);
                    asynchJobSchedulerService.saveBussinesError(jobId, e);
                }catch(RuntimeException e){
                    LOGGER.error(String.format("Nieoczekiwane błąd podczas zadania asynchronicznego o numerze [%s] ", jobId), e);
                    asynchJobSchedulerService.saveRuntimeError(jobId, e);
                }
            }
        }while(jobId != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NoLog
    public Long reserveAsynchronizeJob(){
        AsynchronizeJob job = asynchronizeJobRepository.findFirstAsynchronizeJobToWork();
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.P);
            job.setStartTimestamp(new Date());
            job = asynchronizeJobRepository.update(job, job.getId());
            return job.getId();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void successEndJob(AsynchronizeJobResultInfoDTO resultDTO){
        AsynchronizeJob job = asynchronizeJobRepository.get(resultDTO.getId());
        if(job != null) {
            job.setStatus(Strings.isNullOrEmpty(resultDTO.getStatus()) ? AsynchronizeJobStatus.S :
                                                AsynchronizeJobStatus.valueOf(resultDTO.getStatus()));
            job.setStopTimestamp(new Date());
            job.setDescription(Strings.isNullOrEmpty(resultDTO.getDescription()) ? "Zadanie zakończono sukcesem." :
                                                resultDTO.getDescription());
            asynchronizeJobRepository.update(job, job.getId());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AsynchronizeJobInfoDTO getAsynchronizeJobInfoDTO(Long jobId){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        JobType jobType = jobTypeRepository.findByName(job.getType());
        if(job != null) {
            AsynchronizeJobInfoDTO dto = new AsynchronizeJobInfoDTO();
            dto.setId(job.getId());
            dto.setServiceName(jobType.getJobName());
            dto.setTypeParams(jobType.getServiceNname());
            dto.setParams(job.getParams());
            dto.setOrderId(job.getOrderId());
            return dto;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBussinesError(Long jobId, EntityValidationException e){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.E);
            job.setStopTimestamp(new Date());
            job.setDescription(GryfStringUtils.substring("Wystapił bład biznesowy: " + createBussinesViolation(e),
                                                        0, AsynchronizeJob.DESCRIPTION_MAX_SIZE));
            asynchronizeJobRepository.update(job, job.getId());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRuntimeError(Long jobId, RuntimeException e){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.F);
            job.setStopTimestamp(new Date());
            job.setDescription(GryfStringUtils.substring("Wystapił bład krytyczny: " + e.getMessage(),
                                                        0, AsynchronizeJob.DESCRIPTION_MAX_SIZE));

            asynchronizeJobRepository.update(job, job.getId());
        }
    }

    @Override
    public List<AsynchJobSearchResultDTO> findAsynchJobs(AsynchJobSearchQueryDTO queryDTO) {
        return asynchJobSearchDao.findAsynchJobs(queryDTO);
    }

    @Override
    public AsynchJobDetailsDTO findAsynchJobDetails(Long jobId) {
        AsynchJobDetailsDTO dto = asynchJobSearchDao.findAsynchJobDetails(jobId);
        dto.calculateDurationTime();
        return dto;
    }

    @Override
    public List<ImportDataRowSearchResultDTO> findImportDataRows(ImportDataRowSearchQueryDTO queryDTO) {
        return asynchJobSearchDao.findImportDataRows(queryDTO);
    }

    @Override
    public Map<String, String> getJobStatuses() {
        return AsynchronizeJobStatus.getAsMap();
    }

    @Override
    public Map<String, String> getJobTypes(Optional<Long> grantProgramId, boolean onlyImportJobs) {
        List<JobType> jobTypes;
        if (onlyImportJobs && grantProgramId.isPresent()) {
            jobTypes = jobTypeRepository.findByGrantProgramId(grantProgramId.get().longValue());
        } else {
           jobTypes = jobTypeRepository.findAll();
        }
        return jobTypes.stream().collect(Collectors.toMap(jobType -> jobType.getName(), jobType -> jobType.getLabel()));
    }

    @Override
    public Map<String, String> getImportDataRowsStatuses() {
        return ImportDataRowStatus.getAsMap();
    }

    //PRIVATE METHODS

    private String createBussinesViolation(EntityValidationException e){
        StringBuilder sb = new StringBuilder();
        for(EntityConstraintViolation v : e.getViolations()){
            sb.append(v.getMessage()).append(";");
        }
        return sb.toString();
    }

}
