package pl.sodexo.it.gryf.dao.impl.crud.repository.importdata;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.importdata.ImportDataRowRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.importdata.ImportDataRow;
import pl.sodexo.it.gryf.model.importdata.ImportDataRowStatus;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Isolution on 2016-12-01.
 */
@Repository
public class ImportDataRowRepositoryImpl extends GenericRepositoryImpl<ImportDataRow, Long> implements ImportDataRowRepository {

    @Override
    public ImportDataRow getByImportJobAndRowNum(Long importJobId, Integer rowNum){
        TypedQuery<ImportDataRow> query = entityManager.createNamedQuery("ImportDataRow.getByImportJobAndRowNum", ImportDataRow.class);
        query.setParameter("importJobId", importJobId);
        query.setParameter("rowNum", rowNum);

        List<ImportDataRow> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public int saveRowsForJob(Long importJobId, ImportDataRowStatus defaultStatus, String user, int n){
        Query query = entityManager.createNativeQuery("insert into APP_PBE.IMPORT_DATA_ROWS (ID, IMPORT_JOB_ID, ROW_NUM, STATUS, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP) "
                                                    + "select EAGLE.pk_seq.nextval, ?, rownum, ?, 1, ?, CURRENT_DATE, ?, CURRENT_DATE "
                                                    + "from dual CONNECT BY LEVEL <= ? ");
        query.setParameter(1, importJobId);
        query.setParameter(2, defaultStatus.name());
        query.setParameter(3, user);
        query.setParameter(4, user);
        query.setParameter(5, n);
        return query.executeUpdate();




    }
}
