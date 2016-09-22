package pl.sodexo.it.gryf.service.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationStatus;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationStatusRepository;
import pl.sodexo.it.gryf.service.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;

import java.util.Date;


@Service
public class Reject1ActionService extends ActionBaseService {

    //FIELDS

    @Autowired
    private MailService mailService;

    @Autowired
    private GrantApplicationsService grantApplicationsService;

    @Autowired
    private GrantApplicationStatusRepository grantApplicationStatusRepository;

    //PUBLIC METHODS

    @Override
    protected void executeAction(Order order){
        GrantApplication application = order.getApplication();
        GrantProgram program = application.getProgram();

        //CHANGE GRANT APP STATUS
        application.setStatus(grantApplicationStatusRepository.get(GrantApplicationStatus.REJECTED_CODE));
        //SET DATE
        application.setConsiderationDate(new Date());

        //WYSLANIE MAILA
        OrderElement oeReason = order.loadElement("REJREASON1");
        OrderElement oeAddress = order.loadElement("REJEMAIL1");
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("rejectionReason", oeReason.getValueVarchar())
                                                                                    .add("grantProgramName", program.getProgramName());
        grantApplicationsService.sendPublicGrantProgramEmail(application, EmailTemplate.GA_REJECT, mailPlaceholders, oeAddress.getValueVarchar(), null);
    }

}
