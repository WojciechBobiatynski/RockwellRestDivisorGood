package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.PATH_ELECTRONIC_REIMBURSEMENTS;

/**
 * Kontroler dla elektornicznych rozliczeń
 *
 * Created by akmiecinski on 14.11.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + PATH_ELECTRONIC_REIMBURSEMENTS, produces = "application/json;charset=UTF-8")
public class ElectronicReimbursementsRestController {

}
