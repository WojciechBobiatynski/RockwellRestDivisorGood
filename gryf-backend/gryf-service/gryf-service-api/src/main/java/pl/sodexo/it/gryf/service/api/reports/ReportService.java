package pl.sodexo.it.gryf.service.api.reports;

import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;

import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReportService {

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, ReportSourceType reportSourceType, Long sourceId);

    String generateReport(ReportTemplateCode templateCode, String reportFileName, FileType fileType, Map<String, Object> parameters, ReportSourceType reportSourceType, Long sourceId);

    /**
     * Nota obciążeniowo-ksiegowa
     * @param orderId
     * @param invoiceNumber
     * @return
     */
    String generateDebitNoteForOrder(Long orderId, String invoiceNumber);

    /**
     * Nota uznaniowa
     * @param reimbursmentId
     * @param invoiceNumber
     * @return
     */
    String generateCreditNoteForReimbursment(Long reimbursmentId, String invoiceNumber);

    /**
     * Potwierdzenie wpłaty należnosci
     * @param reimbursmentId
     * @return
     */
    String generateBankTransferConfirmationForReimbursment(Long reimbursmentId);

    /**
     * Potwierdzenie realizacji dofinansowania
     * @param reimbursmentId
     * @return
     */
    String generateGrantAidConfirmationForReimbursment(Long reimbursmentId);
}
