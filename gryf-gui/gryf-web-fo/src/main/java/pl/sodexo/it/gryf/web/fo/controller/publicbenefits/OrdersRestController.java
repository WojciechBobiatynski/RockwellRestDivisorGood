package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.common.parsers.OrderParser;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderActionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;
import pl.sodexo.it.gryf.web.fo.utils.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/order", produces = "application/json;charset=UTF-8")
public class OrdersRestController {

    //FIELDS

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderActionService orderActionService;

    //PUBLIC METHODS

    @RequestMapping(value = "/preview/{id}", method = RequestMethod.GET)
    public String getOrderDataToView(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_ORDERS);
        return orderService.getOrderDataToPreview(id);
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getOrderDataToEdit(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_ORDERS_MOD);
        return orderService.getOrderDataToModify(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<OrderSearchResultDTO> findOrders(OrderSearchQueryDTO searchDTO) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_ORDERS);
        return orderService.findOrders(searchDTO);
    }

    @RequestMapping(value = "/load/{contractId}", method = RequestMethod.GET)
    public CreateOrderDTO getCreateOrderDTO(@PathVariable Long contractId) {
        return orderService.getCreateOrderDTO(contractId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Long saveOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return orderService.saveOrder(createOrderDTO);
    }

    @RequestMapping(value = "/action/{orderId}/{actionId}/{version}", method = RequestMethod.POST)
    public Long executeAction(@PathVariable Long orderId,
                              @PathVariable Long actionId,
                              @PathVariable Integer version,
                              @RequestParam("incomingOrderElementsParam") String incomingOrderElementsParam,
                              @RequestParam(value = "acceptedViolationsParam", required = false) String acceptedViolationsParam,
                              @RequestParam("file") MultipartFile[] files) throws IOException {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_ORDERS_MOD);

        List<FileDTO> fileDtoList = WebUtils.createFileDtoList(files);
        List<IncomingOrderElementDTO> incomingOrderElements = OrderParser.readIncomingOrderElements(incomingOrderElementsParam);
        List<String> acceptedViolations = OrderParser.readAcceptedViolations(acceptedViolationsParam);
        try {
            orderActionService.executeAction(orderId, actionId, version, incomingOrderElements, fileDtoList, acceptedViolations);
        } catch (GryfOptimisticLockRuntimeException e) {
            orderService.manageLocking(orderId);
        }
        return orderId;
    }

}
