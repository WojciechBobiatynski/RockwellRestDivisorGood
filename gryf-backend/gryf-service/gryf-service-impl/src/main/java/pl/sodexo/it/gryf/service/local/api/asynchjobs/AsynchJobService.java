package pl.sodexo.it.gryf.service.local.api.asynchjobs;

import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;

/**
 * Created by Isolution on 2016-12-02.
 */
public interface AsynchJobService {

    void processAsynchronizeJob(AsynchronizeJobInfoDTO dto);
}
