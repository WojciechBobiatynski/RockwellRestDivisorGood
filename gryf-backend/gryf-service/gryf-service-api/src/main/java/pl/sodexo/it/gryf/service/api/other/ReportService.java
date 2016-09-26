package pl.sodexo.it.gryf.service.api.other;

import pl.sodexo.it.gryf.common.ReportTemplateCode;
import pl.sodexo.it.gryf.model.FileType;

import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReportService {

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType);

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, Map<String, Object> parameters);
}
