package pl.sodexo.it.gryf.service.api.reports;

import pl.sodexo.it.gryf.common.FileType;
import pl.sodexo.it.gryf.common.ReportTemplateCode;

import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReportService {

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType);

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, Map<String, Object> parameters);
}
