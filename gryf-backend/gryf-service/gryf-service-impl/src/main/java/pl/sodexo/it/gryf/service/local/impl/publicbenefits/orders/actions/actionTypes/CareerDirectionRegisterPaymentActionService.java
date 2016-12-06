package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionRegisterPaymentActionService extends ActionBaseService {

    //STATIC METHDS

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionGenerateDocumentsActionService.class);

    //PRIVATE METHODS

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS

    @Override
    public void execute(Order order, List<String> acceptedPathViolations){
        LOGGER.debug("Rejestruje wpłatę dla zamówienia [{}]", order.getId());

        //ELEMENTY
        OrderElement oePayAmount = order.getElement(OrderElementCons.KK_PAY_AMOUNT_ELEM_ID);
        OrderElement oeOwnContributionAmount = order.getElement(OrderElementCons.KK_OWN_CONTRIBUTION_AMOUNT_ELEM_ID);
        OrderElement oeConnectAmount = order.getElement(OrderElementCons.KK_CONNECT_AMOUNT_ELEM_ID);
        OrderElement oeConnectDate = order.getElement(OrderElementCons.KK_CONNECT_DATE_ELEM_ID);
        OrderElement oePayDate = order.getElement(OrderElementCons.KK_PAY_DATE_ELEM_ID);

        //VALIDATE ELEMENTS
        List<EntityConstraintViolation> oeViolations = Lists.newArrayList();
        validateOrderElement(oeViolations, order, oePayAmount);
        validateOrderElement(oeViolations, order, oeOwnContributionAmount);
        validateOrderElement(oeViolations, order, oeConnectAmount);
        validateOrderElement(oeViolations, order, oeConnectDate);
        validateOrderElement(oeViolations, order, oePayDate);
        gryfValidator.validate(oeViolations);

        //KWOTY
        BigDecimal payAmount = oePayAmount.getValueNumber();//KWOTA WPLATY
        BigDecimal ownContributionAmount = oeOwnContributionAmount.getValueNumber();//KWOTA WKLADU WLASNEGO
        BigDecimal connectAmount = oeConnectAmount.getValueNumber();//KWOTA PODPIECIA

        //VALIDCJA KWOT
        List<EntityConstraintViolation> bussinesViolations = Lists.newArrayList();
        if(payAmount == null){
            bussinesViolations.add(new EntityConstraintViolation("Kwota wpłaty nie została wypełniona"));
        }
        if(ownContributionAmount == null){
            bussinesViolations.add(new EntityConstraintViolation("Kwota wkładu własnego nie została wypełniona"));
        }
        if(connectAmount == null){
            bussinesViolations.add(new EntityConstraintViolation("Kwota podpięcia nie została wypełniona"));
        }
        if(payAmount.compareTo(ownContributionAmount) < 0){
            bussinesViolations.add(new EntityConstraintViolation(String.format("Kwota wpłaty (%s) nie może być "
                    + "większa od kwoty wkłady własnego (%s)", payAmount, ownContributionAmount)));
        }
        if(connectAmount.compareTo(ownContributionAmount) != 0){
            bussinesViolations.add(new EntityConstraintViolation(String.format("Kwota podpięcia (%s) musi być "
                    + "równa kwocie wkładu własnego (%s)", connectAmount, ownContributionAmount)));
        }
        gryfValidator.validate(bussinesViolations);

        //SET COMPLETE DATE
        Date now = new Date();
        oeOwnContributionAmount.setCompletedDate(now);
        oeConnectAmount.setCompletedDate(now);
        oeConnectDate.setCompletedDate(now);
        oePayDate.setCompletedDate(now);
    }

    //PRIVATE METHODS

    private void validateOrderElement(List<EntityConstraintViolation> violations, Order order, OrderElement oe){
        if(oe == null){
            violations.add(new EntityConstraintViolation(String.format("Na zamówieniu [%s] nie "
                    + "znaleziono pola o identyfikatorze [%s]", order.getId(), oe.getId())));
        }
    }

}
