package pl.sodexo.it.gryf.web.controller.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.Privileges;
import pl.sodexo.it.gryf.dto.dictionaries.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.dictionaries.StateService;
import pl.sodexo.it.gryf.root.service.dictionaries.ZipCodeService;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;

import java.util.List;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
@Controller
@RequestMapping(value = ControllersUrls.DICTIONARIES_REST, produces = "application/json;charset=UTF-8")
public class DictionariesRestController {

    //PRIVATE FIELDS

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ZipCodeService zipCodeService;

    @Autowired
    private StateService stateService;

    //PUBLIC METHODS - SERVICES ZIP CODE

    @RequestMapping(value = "/zipCode/", method = RequestMethod.GET)
    @ResponseBody
    public ZipCode getZipCodeById() {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.createZipCode();
    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ZipCode getZipCodeById(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCode(id);
    }

    @RequestMapping(value = "zipCodes", method = RequestMethod.GET)
    @ResponseBody
    public List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCodes(zipCode);

    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity updateZipCode(@PathVariable Long id, @RequestBody ZipCode zipCode) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        GryfUtils.checkForUpdate(id, zipCode.getId());

        zipCodeService.updateZipCode(zipCode);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/zipCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveZipCode(@RequestBody ZipCode zipCode) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        zipCodeService.saveZipCode(zipCode);
        return ResponseEntity.noContent().build();
    }

    //PUBLIC METHODS - SERVICES STATES

    @RequestMapping(value = "states", method = RequestMethod.GET)
    @ResponseBody
    public List<State> findStates() {
        return stateService.findStatesInPoland();
    }

}
