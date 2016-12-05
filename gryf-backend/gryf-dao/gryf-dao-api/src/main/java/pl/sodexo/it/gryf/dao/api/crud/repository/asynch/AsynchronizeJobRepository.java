package pl.sodexo.it.gryf.dao.api.crud.repository.asynch;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;

/**
 * Created by Isolution on 2016-12-01.
 */
public interface AsynchronizeJobRepository extends GenericRepository<AsynchronizeJob, Long> {

    AsynchronizeJob findFirstAsynchronizeJobToWork();

}
