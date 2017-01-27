package pl.sodexo.it.gryf.service.local.api.reports;

import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.model.reports.ReportInstance;

import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReportService {

    ReportInstance generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, ReportSourceType reportSourceType, Long sourceId);

    ReportInstance generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, Map<String, Object> parameters, ReportSourceType reportSourceType, Long sourceId);

    /**
     * Nota obciążeniowo-ksiegowa
     * @param orderId
     * @param invoiceNumber
     * @return
     */
    ReportInstance generateDebitNoteForOrder(Long orderId, String invoiceNumber);

    /**
     * Nota uznaniowa
     * @param reimbursmentId
     * @param invoiceNumber
     * @return
     */
    ReportInstance generateCreditNoteForReimbursment(Long reimbursmentId, String invoiceNumber);

    /**
     * Potwierdzenie wpłaty należnosci
     * @param reimbursmentId
     * @return
     */
    ReportInstance generateBankTransferConfirmationForReimbursment(Long reimbursmentId);

    /**
     * Potwierdzenie realizacji dofinansowania
     * @param reimbursmentId
     * @return
     */
    ReportInstance generateGrantAidConfirmationForReimbursment(Long reimbursmentId);
}
