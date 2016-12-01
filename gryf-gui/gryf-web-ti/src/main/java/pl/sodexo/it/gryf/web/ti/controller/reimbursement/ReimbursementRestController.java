package pl.sodexo.it.gryf.web.ti.controller.reimbursement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.common.util.WebUtils;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static pl.sodexo.it.gryf.web.common.util.WebUtils.writeFileToResponse;
import static pl.sodexo.it.gryf.web.ti.util.UrlConstants.*;

/**
 * Kontroler do obłsugi rozliczeń dla instytucji szkoleniowej
 *
 * Created by akmiecinski on 15.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PATH_REIMBURSEMENT_REST, produces = "application/json;charset=UTF-8")
public class ReimbursementRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReimbursementRestController.class);

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private ErmbsAttachmentService ermbsAttachmentService;

    @RequestMapping(value = PATH_REIMBURSEMENT_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<ElctRmbsDto> findElctRmbsByCriteria(ElctRmbsCriteria elctRmbsCriteria){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findEcltRmbsListByCriteria(elctRmbsCriteria);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_STATUSES_LIST, method = RequestMethod.GET)
    @ResponseBody
    public List<SimpleDictionaryDto> findElctRmbsStatuses(){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findElctRmbsStatuses();
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_CREATE + "/{trainingInstanceId}", method = RequestMethod.GET)
    @ResponseBody
    public ElctRmbsHeadDto createRmbsForTrainingInstance(@PathVariable Long trainingInstanceId){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.createRmbsDtoByTrainingInstanceId(trainingInstanceId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_MODIFY + "/{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public ElctRmbsHeadDto modifyRmbsForTrainingInstance(@PathVariable Long ermbsId){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_SAVE, method = RequestMethod.POST)
    public ElctRmbsHeadDto saveReimbursement(MultipartHttpServletRequest request) throws IOException {
//        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);
        ElctRmbsHeadDto dto = getElctRmbsHeadDtoFromMultipartRequest(request);
        Long ermbsId = electronicReimbursementsService.saveErmbs(dto);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_SEND, method = RequestMethod.POST)
    public ElctRmbsHeadDto sendToReimburse(MultipartHttpServletRequest request) throws IOException {
//        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);
        ElctRmbsHeadDto dto = getElctRmbsHeadDtoFromMultipartRequest(request);
        Long ermbsId = electronicReimbursementsService.sendToReimburse(dto);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_SAVE_WITH_CORR, method = RequestMethod.POST)
    public ElctRmbsHeadDto saveReimbursementWithCorr(MultipartHttpServletRequest request) throws IOException {
        //        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);
        ElctRmbsHeadDto dto = getElctRmbsHeadDtoFromMultipartRequest(request);
        Long ermbsId = electronicReimbursementsService.saveErmbsWithCorr(dto);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_SEND_WITH_CORR, method = RequestMethod.POST)
    public ElctRmbsHeadDto sendToReimburseWithCorr(MultipartHttpServletRequest request) throws IOException {
        //        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);
        ElctRmbsHeadDto dto = getElctRmbsHeadDtoFromMultipartRequest(request);
        Long ermbsId = electronicReimbursementsService.sendToReimburseWithCorr(dto);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(PATH_REIMBURSEMENTS_DOWNLOAD_ATT)
    public void downloadReimbursementAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = ermbsAttachmentService.getErmbsAttFileById(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    private ElctRmbsHeadDto getElctRmbsHeadDtoFromMultipartRequest(MultipartHttpServletRequest request) throws IOException {
        ElctRmbsHeadDto dto = JsonMapperUtils.readValue(request.getParameter("model"), ElctRmbsHeadDto.class);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        WebUtils.fillErmbsDtoWithAttachments(fileMap, dto);
        return dto;
    }

}
