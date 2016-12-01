package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.JsonMapperUtils.writeValueAsString;
import static pl.sodexo.it.gryf.web.common.util.PageUtil.getURLWithContextPath;
import static pl.sodexo.it.gryf.web.fo.utils.FoPageConstant.VER_CODE_GENERATE_PATH;
import static pl.sodexo.it.gryf.web.fo.utils.FoPageConstant.VER_CODE_SEND_PATH;

;

@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/individual",
        produces = "application/json;charset=UTF-8")
public class IndividualsRestController {

    @Autowired
    private IndividualService individualService;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    GryfAccessCodeGenerator gryfAccessCodeGenerator;

    @Autowired
    private IndividualUserService individualUserService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndividualDto getNewIndividual() {
        //TODO uprawnienia
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.createIndividual();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveIndividual(@RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup, @RequestBody IndividualDto individualDto) {
        //TODO uprawnienia
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISE_MOD);
        return  individualService.saveIndividual(individualDto, checkPeselDup, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IndividualDto getIndividual(@PathVariable Long id) {
        //TODO uprawnienia
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividual(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateIndividual(@PathVariable Long id,
                                 @RequestParam(value = "checkPeselDup", required = false, defaultValue = "true") boolean checkPeselDup, @RequestBody IndividualDto individualDto) {
        //TODO uprawnienia
        securityChecker.assertServicePrivilege(Privileges.GRF_ENTERPRISE_MOD);
        GryfUtils.checkForUpdate(id, individualDto.getId());

        individualService.updateIndividual(individualDto, checkPeselDup, true);
        return individualDto.getId();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO dto) {
        //TODO uprawnienia
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        return individualService.findIndividuals(dto);

    }

    @RequestMapping(value = VER_CODE_GENERATE_PATH + "/{id}", method = RequestMethod.GET)
    public String getNewVerificationCode(@PathVariable Long id) {
        //TODO uprawnienia
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        String newVerificationCode = gryfAccessCodeGenerator.createVerificationCode();
        GryfIndUserDto gryfIndUserDto = individualUserService.saveNewVerificationCodeForIndividual(id, newVerificationCode);
        return writeValueAsString(gryfIndUserDto.getVerificationCode());
    }

    @RequestMapping(value = VER_CODE_SEND_PATH, method = RequestMethod.POST)
    public void sendMailWithVerCode(@RequestBody IndividualDto individualDto, HttpServletRequest request){
        individualService.sendEmailNotification(individualDto, getURLWithContextPath(request));
    }
}
