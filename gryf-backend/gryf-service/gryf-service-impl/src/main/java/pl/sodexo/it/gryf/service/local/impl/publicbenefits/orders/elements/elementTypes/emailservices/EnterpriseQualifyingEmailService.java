package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.emailservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationEmailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;

import java.text.NumberFormat;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class EnterpriseQualifyingEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private OrderServiceLocal orderServiceLocal;
    
    @Autowired
    private GrantApplicationEmailService grantApplicationEmailService;
    
    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", order.getApplication().getProgram().getProgramName())
                                                                   .add("grantedVouchersNumber",order.getVouchersNumber().toString())
                                                                   .add("paymentAmount", NumberFormat.getCurrencyInstance().format(orderServiceLocal.getPaymentAmount(order)))
                                                                   .add("accountNumber", order.getEnterprise() != null ? order.getContract().getAccountPayment() : (order.getContract() != null && order.getContract().getIndividual() != null ?
                                                                                                                            order.getContract().getAccountPayment() : null)  )
                                                                   .add("grantAppAddressCorr", order.getApplication().getBasicData().getAddressCorr())
                                                                   .add("grantAppZipCorr", order.getApplication().getBasicData().getZipCodeCorr().getZipCode())
                                                                   .add("grantAppCityCorr", order.getApplication().getBasicData().getZipCodeCorr().getCityName());
        return mailService.createMailDTO(EmailTemplate.GA_QUALIFY, 
                                         mailPlaceholders,
                                         GryfUtils.formatEmailRecipientsSet(grantApplicationEmailService.getEmailRecipients(order.getApplication(), null)));
    }
    
}
