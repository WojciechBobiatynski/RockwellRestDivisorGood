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
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.common.util.WebUtils;
import pl.sodexo.it.gryf.web.ti.util.UrlConstants;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

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

    @RequestMapping(value = PATH_REIMBURSEMENTS_CREATE, method = RequestMethod.POST)
    @ResponseBody
    public Long createRmbsForTrainingInstance(@RequestBody Long trainingInstanceId){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.createRmbsByTrainingInstanceId(trainingInstanceId);
    }

    @RequestMapping(value = PATH_REIMBURSEMENTS_MODIFY + "/{ermbsId}", method = RequestMethod.GET)
    @ResponseBody
    public ElctRmbsHeadDto modifyRmbsForTrainingInstance(@PathVariable Long ermbsId){
        //        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        return electronicReimbursementsService.findEcltRmbsById(ermbsId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Long saveReimbursement(MultipartHttpServletRequest request) throws IOException {
//        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_REIMB_MOD);

        Collection<List<MultipartFile>> values = request.getMultiFileMap().values();
        MultipartFile[] files = new MultipartFile[values.size()];
        IntStream.range(0, values.size()).forEach(index -> files[index] = values.stream().map(multipartFiles -> multipartFiles.get(0)).findFirst().get());
        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        return 1L;
    }


}
