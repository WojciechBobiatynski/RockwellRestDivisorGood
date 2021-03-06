package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobInfoDTO;
import pl.sodexo.it.gryf.common.dto.asynchjobs.AsynchronizeJobResultInfoDTO;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderActionService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.asynchjobs.AsynchJobService;

/**
 * Created by Isolution on 2016-12-05.
 */
@Service("asynchJobOrderTransitionService")
public class AsynchJobOrderTransitionServiceImpl implements AsynchJobService {

    @Autowired
    private OrderActionService orderActionService;

    @Autowired
    private GryfValidator gryfValidator;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsynchronizeJobResultInfoDTO processAsynchronizeJob(AsynchronizeJobInfoDTO dto) {

        if(!orderActionService.isActionAutomatic(dto.getOrderId(), dto.getParams())) {
            gryfValidator.validate("Status zamówienie nie został zmieniony, ponieważ akcja przejścia nie jest automatyczna. "
                    + "Należy ustawić przejście w tabelce ORDER_FLOW_STATUS_TRANSITIONS w kolumnie AUTOMATIC.");
        }

        boolean flag;
        do{
            flag = orderActionService.executeAutomaticActions(dto.getOrderId());
        }while(flag);

        return new AsynchronizeJobResultInfoDTO(dto.getId(), "Status zamówienie został zmieniony");
    }
}
