package pl.sodexo.it.gryf.service.impl.reports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportParameter;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.report.ReportInstanceRepository;
import pl.sodexo.it.gryf.model.reports.ReportInstance;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.FileService;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomasz.bilski.ext on 2015-08-18.
 */
@Service
public class ReportServiceImpl implements ReportService {

    //FIELDS

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FileService fileService;
    
    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private ReportInstanceRepository reportInstanceRepository;

    //PUBLIC METHODS

    @Override
    public String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType,
                                ReportSourceType reportSourceType, Long sourceId) {
        return generateReport(templateCode, reportFileName, fileType, new HashMap<>(), reportSourceType, sourceId);
    }

    @Override
    public String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, Map<String, Object> parameters,
                                    ReportSourceType reportSourceType, Long sourceId) {
        try {
            //ADD STD PARAMS
            parameters.put(ReportParameter.IMAGES_PATH.getParam(),
                    applicationParameters.getPathAttachments() + applicationParameters.getPathReportTemplates() + applicationParameters.getPathReportImages());

            //MAIN PARAMETERS
            Map<String, Object> mainParameters = new HashedMap(parameters);

            try (Connection connection = dataSource.getConnection()){
                //GENERATE REPORT
                String templatePath = fileService.findPath(FileType.REPORT_TEMPLATES) + templateCode.getFileName();
                InputStream templateInputStream = fileService.getInputStream(templatePath);
                JasperPrint jasperPrint = JasperFillManager.fillReport(templateInputStream, parameters, connection);
                templateInputStream.close();
    
                //OUTPUT TO PDF
                String reportPath = fileService.findPath(fileType) + reportFileName;
                OutputStream reportOutputStream = fileService.getOutputStream(reportPath);
                JasperExportManager.exportReportToPdfStream(jasperPrint, reportOutputStream);
                reportOutputStream.close();

                //SAVE REPORT
                ReportInstance reportInstance = new ReportInstance();
                reportInstance.setTemplateName(templateCode.getFileName());
                String loggedUser= GryfUser.getLoggedUser().getUsername();
                reportInstance.setCreatedUser(loggedUser);
                reportInstance.setCreatedTimestamp(new Date());
                reportInstance.setParameters(JsonMapperUtils.writeValueAsString(mainParameters));
                reportInstance.setPath(reportPath);
                reportInstance.setSourceType(reportSourceType.name());
                reportInstance.setSourceId(sourceId);
                reportInstanceRepository.save(reportInstance);

                return reportPath;
            }
        }catch(IOException e){
            throw new RuntimeException("Wystapił błąd pliku", e);
        }catch(SQLException e){
            throw new RuntimeException("Nie udało się nazwiazać połączenia przy generowaniu raportu", e);
        } catch(JRException e) {
            throw new RuntimeException("Wystapił bład przy generowaniu raportu", e);
        }
    }

}
