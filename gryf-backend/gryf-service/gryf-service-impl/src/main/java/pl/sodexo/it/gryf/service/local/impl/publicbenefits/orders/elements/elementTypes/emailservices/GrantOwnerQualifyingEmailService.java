package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.emailservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class GrantOwnerQualifyingEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;

    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        
        String name = null, regNum = null, invoiceAddress = null, invoiceZipCode = null, invoiceCityName = null;
        if (order.getEnterprise() != null) {
            name = order.getEnterprise().getName();
            regNum = order.getEnterprise().getVatRegNum();
            invoiceAddress = order.getEnterprise().getAddressInvoice();
            invoiceZipCode = order.getEnterprise().getZipCodeInvoice().getZipCode();
            invoiceCityName = order.getEnterprise().getZipCodeInvoice().getCityName();
        } else if(order.getContract() != null && order.getContract().getIndividual() != null){
            name = order.getContract().getIndividual().getFirstName() + " " + order.getContract().getIndividual().getLastName();
            regNum = order.getContract().getIndividual().getPesel();
            invoiceAddress = order.getContract().getIndividual().getAddressInvoice();
            invoiceZipCode = order.getContract().getIndividual().getZipCodeInvoice().getZipCode();
            invoiceCityName = order.getContract().getIndividual().getZipCodeInvoice().getCityName();
        }
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", order.getApplication().getProgram().getProgramName())
                                                                   .add("grantedVouchersNumber",order.getVouchersNumber().toString())
                                                                   .add("enterpriseName", name)
                                                                   .add("enterpriseVatRegNum", regNum)
                                                                   .add("enterpriseAddress", invoiceAddress)
                                                                   .add("enterpriseZipCode", invoiceZipCode)
                                                                   .add("enterpriseCityName", invoiceCityName);
        return mailService.createMailDTO(EmailTemplate.GA_QLY_GO, 
                                         mailPlaceholders,
                                         order.getApplication().getProgram().getGrantOwner().getEmailAddressesGrantAppInfo());
    }
    
}
