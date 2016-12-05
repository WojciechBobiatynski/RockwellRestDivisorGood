package pl.sodexo.it.gryf.dao.impl.crud.repository.importdata;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowErrorRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowError;

/**
 * Created by Isolution on 2016-12-01.
 */
@Repository
public class ImportDataRowErrorRepositoryImpl extends GenericRepositoryImpl<ImportDataRowError, Long> implements ImportDataRowErrorRepository {
}
