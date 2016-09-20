package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.Privileges;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderParser;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.web.controller.ControllersUrls;
import pl.sodexo.it.gryf.web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@RestController
@RequestMapping(value = ControllersUrls.PUBLIC_BENEFITS_REST + "/order", produces = "application/json;charset=UTF-8")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OrdersRestController {

    //FIELDS

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private OrderService orderService;

    //PUBLIC METHODS

    @RequestMapping(value = "/preview/{id}", method = RequestMethod.GET)
    public String getOrderDataToView(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_ORDERS);
        return orderService.getOrderDataToPreview(id);
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getOrderDataToEdit(@PathVariable Long id) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_ORDERS_MOD);
        return orderService.getOrderDataToModify(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<OrderSearchResultDTO> findOrders(OrderSearchQueryDTO searchDTO) {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_ORDERS);
        return orderService.findOrders(searchDTO);
    }

    @RequestMapping(value = "/action/{orderId}/{actionId}/{version}", method = RequestMethod.POST)
    public Long executeAction(@PathVariable Long orderId,
                              @PathVariable Long actionId,
                              @PathVariable Integer version,
                              @RequestParam("incomingOrderElementsParam") String incomingOrderElementsParam,
                              @RequestParam(value = "acceptedViolationsParam", required = false) String acceptedViolationsParam,
                              @RequestParam("file") MultipartFile[] files) throws IOException {
        securityCheckerService.assertServicePrivilege(Privileges.GRF_PBE_ORDERS_MOD);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        List<IncomingOrderElementDTO> incomingOrderElements = OrderParser.readIncomingOrderElements(incomingOrderElementsParam);
        List<String> acceptedViolations = OrderParser.readAcceptedViolations(acceptedViolationsParam);
        try {
            orderService.executeAction(orderId, actionId, version, incomingOrderElements, fileDtoList, acceptedViolations);
        } catch (JpaOptimisticLockingFailureException | OptimisticLockException | javax.persistence.OptimisticLockException o) {
            orderService.manageLocking(orderId);
        }
        return orderId;
    }

}
