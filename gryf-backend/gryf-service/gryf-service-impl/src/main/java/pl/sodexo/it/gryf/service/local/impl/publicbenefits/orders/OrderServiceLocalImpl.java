package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProduct;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class OrderServiceLocalImpl implements OrderServiceLocal {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS

    @Override
    public CreateOrderDTO createCreateOrderDTO(Contract contract){
        GrantProgram grantProgram = contract.getGrantProgram();
        OrderFlowForGrantProgram orderForGrPr = paramInDateService.findOrderFlowForGrantProgram(grantProgram.getId(), new Date());
        OrderFlow orderFlow = orderForGrPr.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        return orderFlowService.createCreateOrderDTO(contract);
    }

    @Override
    public Order createOrder(CreateOrderDTO dto) {
        gryfValidator.validate(dto);

        Contract contract = contractRepository.get(dto.getContractId());
        GrantProgram grantProgram = contract.getGrantProgram();
        OrderFlowForGrantProgram orderForGrPr = paramInDateService.findOrderFlowForGrantProgram(grantProgram.getId(), new Date());
        OrderFlow orderFlow = orderForGrPr.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(contract, orderFlow);

        PbeProduct product = order.getPbeProduct();
        GrantProgramParam ocpParam = paramInDateService.findGrantProgramParam(grantProgram.getId(), GrantProgramParam.OWN_CONTRIBUTION_PERCENT, new Date());


        BigDecimal  productInstanceNum = new BigDecimal(dto.getProductInstanceNum());
        BigDecimal  productInstanceAmount = product.getValue();
        BigDecimal ownContributionPercent = new BigDecimal(ocpParam.getValue());

        orderFlowElementService.addElementNumberValue(order, "PRDNUM_KK", productInstanceNum);
        orderFlowElementService.addElementNumberValue(order, "PRDAMO_KK", productInstanceAmount);
        orderFlowElementService.addElementNumberValue(order, "OWNCONP_KK", ownContributionPercent);

        BigDecimal ownContributionAmont = productInstanceNum.multiply(productInstanceAmount).
                                            multiply(ownContributionPercent).divide(new BigDecimal(100));
        BigDecimal grantAmount = productInstanceNum.multiply(productInstanceAmount).subtract(ownContributionAmont);
        BigDecimal orderAmount = grantAmount.add(ownContributionAmont);

        orderFlowElementService.addElementNumberValue(order, "OWNCONA_KK", ownContributionAmont);
        orderFlowElementService.addElementNumberValue(order, "GRAAMO_KK", grantAmount);
        orderFlowElementService.addElementNumberValue(order, "ORDAMO_KK", orderAmount);

        orderFlowElementService.addElementsByOrderStatus(order);

        return order;
    }

    @Override
    public Order createOrder(GrantApplication grantApplication) {
        GrantApplicationVersion grantApplicationVersion = grantApplication.getApplicationVersion();
        OrderFlowForGrantApplicationVersion orderFlowForGraAppVer = paramInDateService.findOrderFlowForGrantApplicationVersion(grantApplicationVersion.getId(), new Date());
        OrderFlow orderFlow = orderFlowForGraAppVer.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(grantApplication, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public BigDecimal getPaymentAmount(Order order) {
        if (order == null)
            return null;
        try {
            return order.getProduct().getPbeTotalValue().subtract(order.getProduct().getPbeAidValue()).multiply(new BigDecimal(order.getVouchersNumber()));
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wyznaczyć kwoty płatności dla przedsiębiorstwa, prawdopodobnie brakujące dane na zamówieniu/produkcie lub niewłaściwa parametryzacja.");
        }
    }

}

