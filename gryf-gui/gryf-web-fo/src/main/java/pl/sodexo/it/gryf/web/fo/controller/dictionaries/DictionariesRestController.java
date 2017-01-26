package pl.sodexo.it.gryf.web.fo.controller.dictionaries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import java.util.List;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
@RestController
@RequestMapping(value = "/rest/dictionaries")
public class DictionariesRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionariesRestController.class);

    //PRIVATE FIELDS

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ZipCodeService zipCodeService;

    @Autowired
    private StateService stateService;

    //PUBLIC METHODS - SERVICES ZIP CODE

    @RequestMapping(value = "/zipCode/", method = RequestMethod.GET)
    public ZipCodeDto getZipCodeById() {
        LOGGER.debug("getZipCodeById");
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.createZipCode();
    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.GET)
    public ZipCodeDto getZipCodeById(@PathVariable Long id) {
        LOGGER.debug("getZipCodeById, id={}", id);
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCode(id);
    }

    @RequestMapping(value = "zipCodes", method = RequestMethod.GET)
    public List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode) {
        LOGGER.debug("findZipCodes, zipCode={}", zipCode);
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES);
        return zipCodeService.findZipCodes(zipCode);

    }

    @RequestMapping(value = "/zipCode/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity updateZipCode(@PathVariable Long id, @RequestBody ZipCodeDto zipCodeDto) {
        LOGGER.debug("findZipCodes, id={}, zipCodeDto={}",id, zipCodeDto);
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        GryfUtils.checkForUpdate(id, zipCodeDto.getId());

        zipCodeService.updateZipCode(zipCodeDto);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/zipCode", method = RequestMethod.POST)
    public ResponseEntity saveZipCode(@RequestBody ZipCodeDto zipCodeDto) {
        LOGGER.debug("findZipCodes, zipCodeDto={}", zipCodeDto);
        securityChecker.assertServicePrivilege(Privileges.GRF_ZIP_CODES_MOD);
        zipCodeService.saveZipCode(zipCodeDto);
        return ResponseEntity.noContent().build();
    }

    //PUBLIC METHODS - SERVICES STATES

    @RequestMapping(value = "states", method = RequestMethod.GET)
    public List<StateDto> findStates() {
        LOGGER.debug("findStates");
        return stateService.findStatesInPoland();
    }

}
