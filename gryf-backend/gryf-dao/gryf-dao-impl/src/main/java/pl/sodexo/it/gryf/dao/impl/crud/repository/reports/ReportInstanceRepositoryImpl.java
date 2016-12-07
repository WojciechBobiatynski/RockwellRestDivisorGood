package pl.sodexo.it.gryf.dao.impl.crud.repository.reports;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.report.ReportInstanceRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.reports.ReportInstance;

/**
 * Created by adziobek on 07.12.2016.
 */
@Repository
public class ReportInstanceRepositoryImpl extends GenericRepositoryImpl<ReportInstance, Long> implements ReportInstanceRepository{
}