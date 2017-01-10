package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.*;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.common.util.WebUtils;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_TYPES_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<SimpleDictionaryDto> findElctRmbsTypes(){
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findElctRmbsTypes();
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
    @ResponseBody
    public ElctRmbsHeadDto confirm(@PathVariable("rmbsId") Long rmbsId) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        Long id = electronicReimbursementsService.confirm(rmbsId);
        return electronicReimbursementsService.findEcltRmbsById(id);
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

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_SEND_EMAILS, method = RequestMethod.POST)
    @ResponseBody
    public ErmbsMailDto sendEmails(MultipartHttpServletRequest request, @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        ErmbsMailDto dto = JsonMapperUtils.readValue(request.getParameter("data"), ErmbsMailDto.class);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        fillErmbsMailAttachmentWithFiles(dto, fileDtoList);
        return ermbsMailService.sendErmbsMail(dto);
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

    private void fillErmbsMailAttachmentWithFiles(ErmbsMailDto dto, List<FileDTO> fileDtoList){
        List<ErmbsMailAttachmentDto> notReportAttachments = dto.getAttachments().stream().filter(ermbsMailAttachmentDto -> !ermbsMailAttachmentDto.isReportFile()).collect(Collectors.toList());
        IntConsumer myConsumer = (index) -> {
            notReportAttachments.get(index).setFile(fileDtoList.get(index));
        };
        IntStream.range(0, notReportAttachments.size()).forEach(myConsumer);
    }

    @RequestMapping(value = PATH_ELECTRONIC_REIMBURSEMENTS_ATT_SAVE, method = RequestMethod.POST)
    @ResponseBody
    public List<ErmbsAttachmentDto> saveAttachments(MultipartHttpServletRequest request, @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS_MOD);
        ElctRmbsHeadDto dto = JsonMapperUtils.readValue(request.getParameter("data"), ElctRmbsHeadDto.class);
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        fillErmbsAttachmentWithFiles(dto, fileDtoList);
        List<Long> attachemntsId = ermbsAttachmentService.manageErmbsAttachments(dto, ErmbsAttachmentStatus.SENDED);
        return ermbsAttachmentService.findErmbsAttachmentsByIds(attachemntsId);
    }

    private void fillErmbsAttachmentWithFiles(ElctRmbsHeadDto dto, List<FileDTO> fileDtoList){
        List<ErmbsAttachmentDto> changedAttachments = dto.getAttachments().stream().filter(ermbsAttachmentDto -> ermbsAttachmentDto.isChanged()).collect(Collectors.toList());
        IntConsumer myConsumer = (index) -> {
            changedAttachments.get(index).setFile(fileDtoList.get(index));
        };
        IntStream.range(0, changedAttachments.size()).forEach(myConsumer);
    }

}
