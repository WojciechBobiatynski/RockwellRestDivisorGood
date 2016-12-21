package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementReportRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementReport;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsReportService;
import pl.sodexo.it.gryf.service.local.api.FileService;

/**
 * Serwis do operacji na raportach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class ErmbsReportServiceImpl implements ErmbsReportService {

    private static final String PDF_SUFFIX = ".pdf";

    @Autowired
    private FileService fileService;

    @Autowired
    private EreimbursementReportRepository ereimbursementReportRepository;

    @Override
    public FileDTO getErmbsReportFileById(Long id) {
        EreimbursementReport report = ereimbursementReportRepository.get(id);
        FileDTO dto = new FileDTO();
        dto.setName(report.getTypeName() + PDF_SUFFIX);
        dto.setInputStream(fileService.getInputStream(report.getFileLocation()));
        return dto;
    }
}
