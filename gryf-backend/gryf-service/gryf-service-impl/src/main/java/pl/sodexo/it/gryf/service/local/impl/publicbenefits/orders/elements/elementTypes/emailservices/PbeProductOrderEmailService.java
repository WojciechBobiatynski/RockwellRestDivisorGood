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
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationEmailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;

import java.text.SimpleDateFormat;

import static pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons.*;
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

        IndividualContact verEmailContact = individualContactRepository.findByIndividualAndContactType(individual.getId(), ContactType.TYPE_VER_EMAIL);

        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("individualName", individual.getFirstName());
        mailPlaceholders.add("individualLastName", individual.getLastName());
        mailPlaceholders.add("productInstanceNum", order.getVouchersNumber().toString());
        mailPlaceholders.add("expiryDate", new SimpleDateFormat(DATE_FORMAT).format(contract.getExpiryDate()));
        mailPlaceholders.add("indUserUrl", applicationParameters.getIndUserUrl());
        mailPlaceholders.add("indLogin", individual.getPesel());
        mailPlaceholders.add("indPassword", individualUser != null ? AEScryptographer.decrypt(individualUser.getVerificationCode()) : "");
        mailPlaceholders.add("grantProgramName", granProgram.getProgramName());
        mailPlaceholders.add("signDate", new SimpleDateFormat(DATE_FORMAT).format(contract.getSignDate()));
        mailPlaceholders.add("noteNumber", "TODO:5235235");//TODO: tbilski uzupenic note jak już bedzie

        MailDTO a = mailService.createMailDTO(GryfConstants.REALIZE_ORDER_TEMPLATE_CODE,
                mailPlaceholders,
                verEmailContact != null ? verEmailContact.getContactData() : null);
        return a;
    }

}
