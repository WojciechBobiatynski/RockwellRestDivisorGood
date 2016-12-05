package pl.sodexo.it.gryf.service.api.asynchjobs;

import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;

/**
 * Created by Isolution on 2016-12-02.
 */
public interface AsynchJobSchedulerService {

    Long saveAsynchronizeJob(String typeStr, String params, String description);

    void processAsynchronizeJob();

    Long reserveAsynchronizeJob();

    AsynchronizeJobInfoDTO getAsynchronizeJobInfoDTO(Long jobId);

    void successEndJob(Long jobId);

    void saveRuntimeError(Long jobId, RuntimeException e);

}
