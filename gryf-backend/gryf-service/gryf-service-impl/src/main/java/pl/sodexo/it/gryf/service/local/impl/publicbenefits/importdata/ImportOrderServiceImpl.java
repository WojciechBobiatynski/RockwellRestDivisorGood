package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.service.api.generator.IdentityGeneratorService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.importdata.ImportDataService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.validation.publicbenefits.orders.OrderValidator;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = ImportDataService.IMPORT_ORDER_SERVICE)
public class ImportOrderServiceImpl extends DefaultImportOrderServiceImpl {

    @Value("${gryf2.service.pattern.externalOrderIdPatternRegexp.wkk:WKK/[0-9]+/[0-9]+}")
    private String externalOrderIdPatternRegexp ;

    @Override
    public String getExternaOrderIdPattern() {
        return externalOrderIdPatternRegexp;
    }

}
