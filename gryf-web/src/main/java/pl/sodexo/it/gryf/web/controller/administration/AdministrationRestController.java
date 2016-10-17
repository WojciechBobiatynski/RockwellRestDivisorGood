package pl.sodexo.it.gryf.web.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductDto;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.service.api.publicbenefits.products.ProductInstanceService;
import pl.sodexo.it.gryf.service.api.publicbenefits.products.ProductService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import java.util.List;

/**
 * Kontroler REST dla zakładki administracja
 *
 * Created by jbentyn on 2016-10-11.
 */
@Controller
@RequestMapping(value = AdministrationRestController.ADMINISTRATION_REST, produces = "application/json;charset=UTF-8")
public class AdministrationRestController {

    //TODO stałe do przeniesinia po rozdzieleniu warów
    public static final String ADMINISTRATION_REST = "rest/administration";
    public static final String PRODUCTS_REST = "products";
    public static final String GENERATE_PRINT_NUMBERS_REST = "/generatePrintNumbers";

    @Autowired
    private ProductInstanceService productInstanceService;

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = PRODUCTS_REST, method = RequestMethod.GET)
    @ResponseBody
    public List<ProductDto> getProducts() {
        return productService.findProducts();
    }

    @RequestMapping(value = GENERATE_PRINT_NUMBERS_REST, method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity generatePrintNumbers(@RequestBody ProductDto productDto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_PRODUCTS_GEN_PRINT_NUM);
        productInstanceService.generatePrintNumbersForProduct(productDto.getProductId());
        return ResponseEntity.noContent().build();
    }

}
