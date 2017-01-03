package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.emailservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;

import java.text.SimpleDateFormat;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.*;

/**
 * Created by Isolution on 2016-11-23.
 */
@Service
public class PbeProductOrderEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private IndividualContactRepository individualContactRepository;

    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        Contract contract = order.getContract();
        Individual individual = contract.getIndividual();
        IndividualUser individualUser = individual.getIndividualUser();
        GrantProgram granProgram = contract.getGrantProgram();
        OrderInvoice orderInvoice = getOrderInvoice(order);

        IndividualContact verEmailContact = individualContactRepository.findByIndividualAndContactType(individual.getId(), ContactType.TYPE_VER_EMAIL);

        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("firstName", individual.getFirstName());
        mailPlaceholders.add("lastName", individual.getLastName());
        mailPlaceholders.add("grantProgramName", granProgram.getProgramName());
        mailPlaceholders.add("grantedVouchersNumber", order.getVouchersNumber().toString());
        mailPlaceholders.add("IndividualWebAppURL", applicationParameters.getIndUserUrl());
        mailPlaceholders.add("IndividualWebAppLogin", individual.getPesel());
        mailPlaceholders.add("IndividualWebAppPass", individualUser != null ? AEScryptographer.decrypt(individualUser.getVerificationCode()) : "");
        mailPlaceholders.add("noteNo", orderInvoice.getInvoiceNumber());
        mailPlaceholders.add("expiryDate", new SimpleDateFormat(DATE_FORMAT).format(contract.getExpiryDate()));
        mailPlaceholders.add("signDate", new SimpleDateFormat(DATE_FORMAT).format(contract.getSignDate()));
        MailDTO a = mailService.createMailDTO(GryfConstants.REALIZE_ORDER_TEMPLATE_CODE,
                mailPlaceholders,
                verEmailContact != null ? verEmailContact.getContactData() : null);
        return a;
    }

    private OrderInvoice getOrderInvoice(Order order){
        List<OrderInvoice> orderInvoices = order.getOrderInvoices();
        if(orderInvoices.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] nie zawiera żadnej noty.", order.getId()));
        }
        if(orderInvoices.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] zawiera wiecej niż jedną notę.", order.getId()));
        }
        return orderInvoices.get(0);
    }

}
