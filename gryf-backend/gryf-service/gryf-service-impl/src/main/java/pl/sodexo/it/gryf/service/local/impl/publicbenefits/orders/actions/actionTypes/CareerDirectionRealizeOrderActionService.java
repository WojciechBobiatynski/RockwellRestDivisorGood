package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.products.PrintNumberChecksumProvider;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionRealizeOrderActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionRealizeOrderActionService.class);

    @Autowired
    private PbeProductInstancePoolStatusRepository productInstancePoolStatusRepository;

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

    @Autowired
    private PbeProductInstancePoolEventTypeRepository productInstancePoolEventTypeRepository;

    @Autowired
    private PbeProductInstancePoolEventRepository productInstancePoolEventRepository;

    @Autowired
    private PbeProductInstanceRepository productInstanceRepository;

    @Autowired
    private GrantProgramProductRepository grantProgramProductRepository;

    @Autowired
    private PbeProductInstanceStatusRepository productInstanceStatusRepository;

    @Autowired
    private PbeProductInstanceEventTypeRepository productInstanceEventTypeRepository;

    @Autowired
    private PbeProductInstanceEventRepository pbeProductInstanceEventRepository;

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations){
        LOGGER.debug("Realizacja zamówienia (utworzenie puli bonów, przypisanie instancji) dla zamówienia [{}]", order.getId());
        //TODO: tbilski - Optimistic Locking exception ??

        Contract contract = order.getContract();
        Individual individual = contract.getIndividual();
        GrantProgram grantProgram = contract.getGrantProgram();
        Integer productInstanceNum = 30;//TODO: tbilski - pole z zamówienia

        //STWORZENIE PULI BONÓW
        PbeProductInstancePool pool = new PbeProductInstancePool();
        pool.setStatus(productInstancePoolStatusRepository.get(PbeProductInstancePoolStatus.ACTIVE_CODE));
        pool.setExpiryDate(contract.getExpiryDate());
        pool.setAllNum(productInstanceNum);
        pool.setAvailableNum(productInstanceNum);
        pool.setReservedNum(0);
        pool.setUsedNum(0);
        pool.setRembursNum(0);
        pool.setIndividual(individual);
        pool.setOrder(order);
        pool = productInstancePoolRepository.save(pool);

        //STWORZENIE EVENTU DO PULI BONÓW
        PbeProductInstancePoolEvent event = new PbeProductInstancePoolEvent();
        event.setProductInstancePool(pool);
        event.setType(productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.ACTIVE_CODE));
        event.setSourceId(order.getId());
        productInstancePoolEventRepository.save(event);

        //POBRANIE INSTANCJI PRODUKTOW
        GrantProgramProduct gpProduct = findGrantProgramProduct(grantProgram.getId(), new Date());
        List<PbeProductInstance> productInstances = productInstanceRepository.findAvaiableByProduct(gpProduct.getPbePproduct().getId(), productInstanceNum);
        if(productInstances.size() != productInstanceNum){
            gryfValidator.validate ("Nie ma wystarczajacej liczby bonów aby zerealizować zamówienie.");
        }

        //POBRANIE STATUSOW I TYPOW DLA REZERWOWANYCH INSTANCJI
        PbeProductInstanceStatus reservedStatus = productInstanceStatusRepository.get(PbeProductInstanceStatus.RES_CODE);
        PbeProductInstanceEventType reservedEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.RES_CODE);

        //ZMIANY NA INSTANCJACH PRODUKTOW
        for(PbeProductInstance pi : productInstances){
            pi.setStatus(reservedStatus);
            pi.setExpiryDate(contract.getExpiryDate());
            pi.setProductInstancePool(pool);

            //GENEROWNAIE PRINT NUM
            PrintNumberResultDto piPrintNumber = generatePrintNumber(gpProduct.getPbePproduct(), contract, pi);
            pi.setPrintNumber(piPrintNumber.getGeneratedPrintNumber());
            pi.setCrc(piPrintNumber.getGeneratedChecksum());
            pi = productInstanceRepository.update(pi, pi.getId());

            //EVENTY DO INSTANCJI PRODUKTOW
            PbeProductInstanceEvent piEvent = new PbeProductInstanceEvent();
            piEvent.setProductInstance(pi);
            piEvent.setType(reservedEventType);
            piEvent.setSourceId(order.getId());
            pbeProductInstanceEventRepository.save(piEvent);
        }
    }

    //PRIVATE METHODS

    /**
     * Metoda wyszukuje obiekt GrantProgramProduct na podstawie id programu doginansowania i daty wpłynięcia wniosku. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu GrantProgramProduct dla zadanych parametrów. .
     * @param
     */
    private GrantProgramProduct findGrantProgramProduct(Long grantProgramId, Date date){
        List<GrantProgramProduct> grantProgramProducts = grantProgramProductRepository.findByGrantProgramInDate(grantProgramId, date);
        if(grantProgramProducts.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Nie znaleziono żadnego "
                                                    + "produktu dla programu [%s] obowiązującego dnia [%s]",
                                                        grantProgramId, date));
        }
        if(grantProgramProducts.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej "
                                                    + "programu [%s] znaleziono więcej niż jeden produkt obowiązujący dnia [%s]",
                                                        grantProgramId, date));
        }
        GrantProgramProduct p = grantProgramProducts.get(0);
        if(p.getPbePproduct() == null){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej programu "
                                                + "[%s] znaleziono jeden jeden produkt obowiązujący dnia [%s] ale bez ustawionej kolumny PBE_PRD_ID",
                                                    grantProgramId, date));
        }
        return p;
    }

    private PrintNumberResultDto generatePrintNumber(PbeProduct product, Contract contract, PbeProductInstance instance){
        PrintNumberDto dto = new PrintNumberDto();
        dto.setFaceValue(product.getValue().multiply(new BigDecimal("100")).intValue());
        dto.setProductInstanceNumber(instance.getId().getNumber());
        dto.setTypeNumber(Integer.valueOf(product.getProductType()));
        dto.setValidDate(contract.getExpiryDate());
        return printNumberGenerator.generate(dto);
    }

}
