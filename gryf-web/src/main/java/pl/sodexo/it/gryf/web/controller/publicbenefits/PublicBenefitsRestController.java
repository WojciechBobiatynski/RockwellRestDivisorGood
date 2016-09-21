package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;
import pl.sodexo.it.gryf.root.service.DictionaryService;
import pl.sodexo.it.gryf.root.service.dictionaries.ContactTypeService;

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

    //PUBLIC METHODS - SERVICES CONTACT TYPES

    @RequestMapping(value = "/contactTypes", method = RequestMethod.GET)
    @ResponseBody
    public List<ContactType> findContactTypes() {
        return contactTypeService.findContactTypes();
    }

    //PUBLIC METHODS - DICTIONARIES

    @RequestMapping(value = "/dictionaries/{dictionaryCodeValue}", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findDictionaries(@PathVariable String dictionaryCodeValue) {
        return dictionaryService.findDictionaries(dictionaryCodeValue);

    }

}
