package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.actions.ActionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-25.
 */
public abstract class ActionBaseService implements ActionService {

    //FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations){

        //VIOLATE VIOLATIONS
        List<EntityConstraintViolation> violateViolations = generateViolateViolations(order);
        gryfValidator.validate(violateViolations);

        //CONFIRM VIOLATIONS
        List<EntityConstraintViolation> confirmViolations = generateConfirmViolations(order);
        List<EntityConstraintViolation> violationsInPath = new ArrayList<>();
        List<EntityConstraintViolation> violationsOutPath = new ArrayList<>();
        gryfValidator.classifyByPath(confirmViolations, acceptedPathViolations, violationsInPath, violationsOutPath);
        gryfValidator.validateWithConfirm(violationsOutPath);

        //EXECUTE ACTION
        executeAction(order);
    }

    //PROTECTED METHODS

    protected List<EntityConstraintViolation> generateViolateViolations(Order order){
        return new ArrayList<>();
    }

    protected List<EntityConstraintViolation> generateConfirmViolations(Order order){
        return new ArrayList<>();
    }

    protected void executeAction(Order order){
    }
}