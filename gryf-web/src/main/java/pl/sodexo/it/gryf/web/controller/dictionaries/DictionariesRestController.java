package pl.sodexo.it.gryf.web.controller.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.StateDto;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.dictionaries.StateService;
import pl.sodexo.it.gryf.service.api.dictionaries.ZipCodeService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.utils.UrlConstants;

import java.util.List;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
@Controller
@RequestMapping(value = UrlConstants.DICTIONARIES_REST, produces = "application/json;charset=UTF-8")
public class DictionariesRestController {

    //PRIVATE FIELDS

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ZipCodeService zipCodeService;

    @Autowired
    private StateService stateService;

    //PUBLIC METHODS - SERVICES ZIP CODE

    @RequestMapping(value = "/zipCode/", method = RequestMethod.GET)
    @ResponseBody
    public ZipCodeDto getZipCodeById() {
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.createZipCode();
    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ZipCodeDto getZipCodeById(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCode(id);
    }

    @RequestMapping(value = "zipCodes", method = RequestMethod.GET)
    @ResponseBody
    public List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCodes(zipCode);

    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity updateZipCode(@PathVariable Long id, @RequestBody ZipCodeDto zipCodeDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        GryfUtils.checkForUpdate(id, zipCodeDto.getId());

        zipCodeService.updateZipCode(zipCodeDto);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/zipCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveZipCode(@RequestBody ZipCodeDto zipCodeDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        zipCodeService.saveZipCode(zipCodeDto);
        return ResponseEntity.noContent().build();
    }

    //PUBLIC METHODS - SERVICES STATES

    @RequestMapping(value = "states", method = RequestMethod.GET)
    @ResponseBody
    public List<StateDto> findStates() {
        return stateService.findStatesInPoland();
    }

}
