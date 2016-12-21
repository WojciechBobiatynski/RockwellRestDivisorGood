package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static pl.sodexo.it.gryf.web.common.util.WebUtils.writeFileToResponse;
import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

/**
 * Kontroler dla elektornicznych rozliczeń
 *
 * Created by akmiecinski on 14.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + PATH_ELECTRONIC_REIMBURSEMENTS, produces = "application/json;charset=UTF-8")
public class ElectronicReimbursementsRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private ErmbsAttachmentService ermbsAttachmentService;

    @Autowired
    private CorrectionService correctionService;

    @Autowired
    private CorrectionAttachmentService correctionAttachmentService;

    @Autowired
    private ErmbsReportService ermbsReportService;

    @Autowired
    private ErmbsMailService ermbsMailService;

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<ElctRmbsDto> findElctRmbsByCriteria(ElctRmbsCriteria elctRmbsCriteria){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findEcltRmbsListByCriteria(elctRmbsCriteria);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_STATUSES_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<SimpleDictionaryDto> findElctRmbsStatuses(){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findElctRmbsStatuses();
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_FIND + "{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public ElctRmbsHeadDto findElctRmbsById(@PathVariable Long ermbsId){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CHANGE_STATUS + "/tocorrect", method = RequestMethod.POST)
    @ResponseBody
    public Long sendToCorrect(@RequestBody CorrectionDto correctionDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
       return electronicReimbursementsService.sendToCorrect(correctionDto);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CORRECTION_DATE, method = RequestMethod.GET)
    @ResponseBody
    public Date getRequiredCorrectionDate() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        return correctionService.getRequiredCorrectionDate();
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CORRECTIONS_LIST + "{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public List<CorrectionDto> findCorrectionsByERmbsId(@PathVariable Long ermbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return correctionService.findCorrectionsByERmbsId(ermbsId);
    }

    @RequestMapping(PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_ATT)
    public void downloadReimbursementAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = ermbsAttachmentService.getErmbsAttFileById(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping(PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_CORR_ATT)
    public void downloadCorrectionAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = correctionAttachmentService.getCorrAttFileById(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CREATE_DOCUMENTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public ElctRmbsHeadDto createDocuments(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.createDocuments(rmbsId);
        return electronicReimbursementsService.findEcltRmbsById(id);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_PRINT_REPORTS + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public ElctRmbsHeadDto printReports(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.printReports(rmbsId);
        return electronicReimbursementsService.findEcltRmbsById(id);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CONFIRM + "{rmbsId}", method = RequestMethod.POST)
    public void confirm(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        electronicReimbursementsService.confirm(rmbsId);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_EXPIRE + "{rmbsId}", method = RequestMethod.POST)
    public void expire(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        electronicReimbursementsService.expire(rmbsId);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CANCEL + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public ElctRmbsHeadDto cancel(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.cancel(rmbsId);
        return electronicReimbursementsService.findEcltRmbsById(id);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_CREATE_EMAIL_FROM_TEMPLATE + "{rmbsId}", method = RequestMethod.POST)
    @ResponseBody
    public List<ErmbsMailDto> createEmailsFromTemplate(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        List<ErmbsMailDto> mailFromTemplates = ermbsMailService.createMailFromTemplates(rmbsId);
        return mailFromTemplates;
    }

    @RequestMapping(PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_REPORT_FILE)
    public void downloadReportFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = ermbsReportService.getErmbsReportFileById(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

}
