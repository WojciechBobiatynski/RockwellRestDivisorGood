package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypePbeProductInfoDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderElementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProduct;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypePbeProductInfoService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Isolution on 2016-11-22.
 */
@Service("orderElementComplexTypePbeProductInfoService")
public class OrderElementComplexTypePbeProductInfoServiceImpl extends OrderElementBaseService<OrderElementComplexTypePbeProductInfoDTO>
                                                                implements OrderElementComplexTypePbeProductInfoService {

    //STATIC FIELDS

    public static final String PRODUCT_INSTANCE_NUM_ELEM_ID = "PRDNUM_KK";

    public static final String PRODUCT_INSTANCE_AMOUNT_ELEM_ID = "PRDAMO_KK";

    public static final String OWN_CONTRIBUTION_PERCENT_ELEM_ID = "OWNCONP_KK";

    public static final String OWN_CONTRIBUTION_AMONT_ELEM_ID = "OWNCONA_KK";

    public static final String GRANT_AMOUNT_ELEM_ID = "GRAAMO_KK";

    public static final String ORDER_AMOUNT_ELEM_ID = "ORDAMO_KK";

    public static final List<String> LIST_ELEM_ID  = new ArrayList<>();

    static{
        LIST_ELEM_ID.add(PRODUCT_INSTANCE_NUM_ELEM_ID);
        LIST_ELEM_ID.add(PRODUCT_INSTANCE_AMOUNT_ELEM_ID);
        LIST_ELEM_ID.add(OWN_CONTRIBUTION_PERCENT_ELEM_ID);
        LIST_ELEM_ID.add(OWN_CONTRIBUTION_AMONT_ELEM_ID);
        LIST_ELEM_ID.add(GRANT_AMOUNT_ELEM_ID);
        LIST_ELEM_ID.add(ORDER_AMOUNT_ELEM_ID);
    }

    //FIELDS

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private OrderElementRepository orderElementRepository;

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypePbeProductInfoDTO createElement(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();

        Map<String, OrderElement> elementMap = orderElementRepository.findByOrderAndElements(order.getId(), LIST_ELEM_ID);
        checkContainsMap(order, elementMap);

        Integer productInstanceNum = (elementMap.get(PRODUCT_INSTANCE_NUM_ELEM_ID).getValueNumber() != null) ?
                                        elementMap.get(PRODUCT_INSTANCE_NUM_ELEM_ID).getValueNumber().intValue() : null;
        BigDecimal productInstanceAmount = elementMap.get(PRODUCT_INSTANCE_AMOUNT_ELEM_ID).getValueNumber();
        BigDecimal ownContributionPercent = elementMap.get(OWN_CONTRIBUTION_PERCENT_ELEM_ID).getValueNumber();
        BigDecimal ownContributionAmont = elementMap.get(OWN_CONTRIBUTION_AMONT_ELEM_ID).getValueNumber();
        BigDecimal grantAmount = elementMap.get(GRANT_AMOUNT_ELEM_ID).getValueNumber();
        BigDecimal orderAmount = elementMap.get(ORDER_AMOUNT_ELEM_ID).getValueNumber();

        //TODO: tbilski odkomentować
        /*return OrderElementDTOProvider.createOrderElementComplexTypePbeProductInfoDTO
                                                        (builder, productInstanceNum, productInstanceAmount,
                                                         ownContributionPercent, ownContributionAmont, grantAmount,
                                                         orderAmount);*/
        return null;
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypePbeProductInfoDTO dto) {
    }

    @Override
    public void addPbeProductElements(Order order, Contract contract, CreateOrderDTO dto) {
        PbeProduct product = order.getPbeProduct();
        GrantProgram grantProgram = contract.getGrantProgram();
        GrantProgramParam ocpParam = paramInDateService.findGrantProgramParam(grantProgram.getId(), GrantProgramParam.OWN_CONTRIBUTION_PERCENT, new Date());

        BigDecimal productInstanceNum = new BigDecimal(dto.getProductInstanceNum());
        BigDecimal  productInstanceAmount = product.getValue();
        BigDecimal ownContributionPercent = new BigDecimal(ocpParam.getValue());

        orderFlowElementService.addElementNumberValue(order, PRODUCT_INSTANCE_NUM_ELEM_ID, productInstanceNum);
        orderFlowElementService.addElementNumberValue(order, PRODUCT_INSTANCE_AMOUNT_ELEM_ID, productInstanceAmount);
        orderFlowElementService.addElementNumberValue(order, OWN_CONTRIBUTION_PERCENT_ELEM_ID, ownContributionPercent);


        BigDecimal ownContributionAmont = productInstanceNum.multiply(productInstanceAmount).
                multiply(ownContributionPercent).divide(new BigDecimal(100));
        BigDecimal grantAmount = productInstanceNum.multiply(productInstanceAmount).subtract(ownContributionAmont);
        BigDecimal orderAmount = grantAmount.add(ownContributionAmont);

        orderFlowElementService.addElementNumberValue(order, OWN_CONTRIBUTION_AMONT_ELEM_ID, ownContributionAmont);
        orderFlowElementService.addElementNumberValue(order, GRANT_AMOUNT_ELEM_ID, grantAmount);
        orderFlowElementService.addElementNumberValue(order, ORDER_AMOUNT_ELEM_ID, orderAmount);
    }

    //PRIVATE METHODS

    private void checkContainsMap(Order order, Map<String, OrderElement> elementMap){
        checkContainsMap(order, elementMap, PRODUCT_INSTANCE_NUM_ELEM_ID);
        checkContainsMap(order, elementMap, PRODUCT_INSTANCE_AMOUNT_ELEM_ID);
        checkContainsMap(order, elementMap, OWN_CONTRIBUTION_PERCENT_ELEM_ID);
        checkContainsMap(order, elementMap, OWN_CONTRIBUTION_AMONT_ELEM_ID);
        checkContainsMap(order, elementMap, GRANT_AMOUNT_ELEM_ID);
        checkContainsMap(order, elementMap, ORDER_AMOUNT_ELEM_ID);
    }

    private void checkContainsMap(Order order, Map<String, OrderElement> elementMap, String key){
        if (!elementMap.containsKey(key)){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    key, order.getId()));
        }
    }
}
