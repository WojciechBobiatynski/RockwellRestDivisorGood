package pl.sodexo.it.gryf.service.impl.asynchjobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobStatus;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJobType;
import pl.sodexo.it.gryf.service.api.asynchjobs.AsynchJobSchedulerService;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import javax.annotation.PostConstruct;

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

    private AsynchJobSchedulerService asynchJobSchedulerService;

    //LIFECYCLE METHODS

    @PostConstruct
    public void init(){
        asynchJobSchedulerService = context.getBean(AsynchJobSchedulerService.class);
    }

    //PUBLIC METHODS

    @Override
    public Long saveAsynchronizeJob(String typeStr, String params, String description){
        AsynchronizeJob job = new AsynchronizeJob();
        job.setType(AsynchronizeJobType.valueOf(typeStr));
        job.setParams(params);
        job.setDescription(description);
        job.setStatus(AsynchronizeJobStatus.N);
        job = asynchronizeJobRepository.save(job);
        return job.getId();
    }

    @Override
    @Scheduled(initialDelay = 15 * 1000, fixedDelay= 15 * 1000)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void processAsynchronizeJob(){
        Long jobId;
        do{
            //RESERVE ONE JOB
            jobId = asynchJobSchedulerService.reserveAsynchronizeJob();

            if(jobId != null){
                try {

                    //ACTION JOB
                    AsynchronizeJobInfoDTO dto = asynchJobSchedulerService.getAsynchronizeJobInfoDTO(jobId);
                    AsynchJobService jobService = (AsynchJobService) BeanUtils.findBean(context, dto.getServiceName());
                    jobService.processAsynchronizeJob(dto);

                    //SET SUCCESS STATUS
                    asynchJobSchedulerService.successEndJob(jobId);

                //OBSLUGA BLEDOW
                }catch(RuntimeException e){
                    LOGGER.error(String.format("Nieoczekiwane błąd podczas zadania asynchronicznego o numerze [%s] ", jobId), e);
                    asynchJobSchedulerService.saveRuntimeError(jobId, e);
                }
            }
        }while(jobId != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long reserveAsynchronizeJob(){
        AsynchronizeJob job = asynchronizeJobRepository.findFirstAsynchronizeJobToWork();
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.P);
            job = asynchronizeJobRepository.update(job, job.getId());
            return job.getId();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void successEndJob(Long jobId){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.S);
            asynchronizeJobRepository.update(job, job.getId());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AsynchronizeJobInfoDTO getAsynchronizeJobInfoDTO(Long jobId){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        if(job != null) {
            AsynchronizeJobInfoDTO dto = new AsynchronizeJobInfoDTO();
            dto.setId(job.getId());
            dto.setServiceName(job.getType().getServiceName());
            dto.setTypeParams(job.getType().getParams());
            dto.setParams(job.getParams());
            return dto;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRuntimeError(Long jobId, RuntimeException e){
        AsynchronizeJob job = asynchronizeJobRepository.get(jobId);
        if(job != null) {
            job.setStatus(AsynchronizeJobStatus.F);
            job.setDescription("Wystapił bład krytyczny: " + e.getMessage());
            asynchronizeJobRepository.update(job, job.getId());
        }
    }

}
