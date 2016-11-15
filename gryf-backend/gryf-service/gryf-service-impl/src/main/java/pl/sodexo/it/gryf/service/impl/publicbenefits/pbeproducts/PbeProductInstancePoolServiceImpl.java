package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstanceEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstancePoolEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
@Service
@Transactional
public class PbeProductInstancePoolServiceImpl implements PbeProductInstancePoolService {

    //PRIVATE FIELDS - REPOSITORY

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PbeProductInstancePoolStatusRepository productInstancePoolStatusRepository;

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

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

    //PRIVATE FIELDS - VALIDATORS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingReservationValidator trainingReservationValidator;

    //PRIVATE FIELDS - OTHERS

    @Autowired
    private PbeProductInstancePoolEventBuilder productInstancePoolEventBuilder;

    @Autowired
    private PbeProductInstanceEventBuilder productInstanceEventBuilder;

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Autowired
    private PbeProductInstancePoolEntityMapper productInstancePoolEntityMapper;

    @Autowired
    private IndividualService individualService;

    //PUBLIC METHODS - FIND

    @Override
    public UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto) {
        IndividualDto individualDto = individualService.findIndividualByPesel(userAuthDataDto.getPesel());
        trainingReservationValidator.validateIndUserAuthorizationData(userAuthDataDto, individualDto);

        return individualService.findUserTrainingReservationData(userAuthDataDto.getPesel());
    }

    //PUBLIC METHODS - ACTIONS

    @Override
    public void createProductInstancePool(Long orderId){
        Order order = orderRepository.get(orderId);
        Contract contract = order.getContract();
        Individual individual = contract.getIndividual();
        GrantProgram grantProgram = contract.getGrantProgram();
        Integer productInstanceNum = 30;//TODO: tbilski - pole z zamówienia

        //STWORZENIE PULI BONÓW
        PbeProductInstancePool pool = createProductInstancePool(order, contract, individual, productInstanceNum);
        pool = productInstancePoolRepository.save(pool);

        //STWORZENIE EVENTU DO PULI BONÓW
        PbeProductInstancePoolEvent event = productInstancePoolEventBuilder.createPbeProductInstancePoolEvent(pool, PbeProductInstancePoolEventType.ACTIVE_CODE, order.getId());
        productInstancePoolEventRepository.save(event);

        //POBRANIE STATUSOW I TYPOW DLA REZERWOWANYCH INSTANCJI
        PbeProductInstanceStatus reservedStatus = productInstanceStatusRepository.get(PbeProductInstanceStatus.RES_CODE);
        PbeProductInstanceEventType reservedEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.RES_CODE);

        //POBRANIE INSTANCJI PRODUKTOW
        List<PbeProductInstance> productInstances = findAvaiableProductInstanceByGrantProgram(grantProgram, productInstanceNum);
        for(PbeProductInstance instance : productInstances){

            //ZMIANY NA INSTANCJACH PRODUKTOW
            reserveProductInstance(instance, reservedStatus, pool, contract);
            instance = productInstanceRepository.update(instance, instance.getId());

            //EVENTY DO INSTANCJI PRODUKTOW
            PbeProductInstanceEvent piEvent = productInstanceEventBuilder.createPbeProductInstanceEvent(instance, reservedEventType, order.getId());
            pbeProductInstanceEventRepository.save(piEvent);
        }

        //TODO: tbilski - Optimistic Locking exception ??
    }

    //PRIVATE METHODS

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

    private PbeProductInstancePool createProductInstancePool(Order order, Contract contract, Individual individual, Integer productInstanceNum){
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
        return pool;
    }

    private void reserveProductInstance(PbeProductInstance instance, PbeProductInstanceStatus reservedStatus,
                                        PbeProductInstancePool pool, Contract contract){
        instance.setStatus(reservedStatus);
        instance.setExpiryDate(contract.getExpiryDate());
        instance.setProductInstancePool(pool);

        //GENEROWNAIE PRINT NUM
        PrintNumberResultDto piPrintNumber = generatePrintNumber(instance.getProductEmission().getProduct(), contract, instance);
        instance.setPrintNumber(piPrintNumber.getGeneratedPrintNumber());
        instance.setCrc(piPrintNumber.getGeneratedChecksum());
    }

    private List<PbeProductInstance> findAvaiableProductInstanceByGrantProgram(GrantProgram grantProgram, Integer productInstanceNum){
        GrantProgramProduct gpProduct = findGrantProgramProduct(grantProgram.getId(), new Date());
        List<PbeProductInstance> productInstances = productInstanceRepository.findAvaiableByProduct(gpProduct.getPbePproduct().getId(), productInstanceNum);
        if(productInstances.size() != productInstanceNum){
            gryfValidator.validate ("Nie ma wystarczajacej liczby bonów aby zerealizować zamówienie.");
        }
        return productInstances;
    }

}
