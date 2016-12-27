package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactTypeDto;
import pl.sodexo.it.gryf.service.api.dictionaries.ContactTypeService;
import pl.sodexo.it.gryf.service.api.other.DictionaryService;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-22.
 */
@Controller
@RequestMapping(value = "/rest/publicBenefits", produces = "application/json;charset=UTF-8")
public class PublicBenefitsRestController {

    @Autowired
    private ContactTypeService contactTypeService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ContractService contractService;

    //PUBLIC METHODS - SERVICES CONTACT TYPES

    @RequestMapping(value = "/contactTypes", method = RequestMethod.GET)
    @ResponseBody
    public List<ContactTypeDto> findContactTypes() {
        return contactTypeService.findContactTypes();
    }

    //PUBLIC METHODS - DICTIONARIES

    @RequestMapping(value = "/dictionaries/{dictionaryCodeValue}", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findDictionaries(@PathVariable String dictionaryCodeValue) {
        return dictionaryService.findDictionaries(dictionaryCodeValue);
    }

    @RequestMapping(value = "/grantPrograms", method = RequestMethod.GET)
    @ResponseBody
    public List<GrantProgramDictionaryDTO> findGrantProgramNames() {
        return contractService.findGrantProgramsDictionaries();
    }

}
