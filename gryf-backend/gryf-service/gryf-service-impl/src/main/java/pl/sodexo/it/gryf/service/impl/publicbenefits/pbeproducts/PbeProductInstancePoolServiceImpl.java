package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproducts;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryCatalogParamRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalogParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceStatus;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstanceEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstancePoolEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private TrainingCategoryCatalogParamRepository trainingCategoryCatalogParamRepository;

    @Autowired
    private PbeProductInstancePoolUseRepository productInstancePoolUseRepository;

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

    @Override
    public void createTrainingInstance(Long trainingId, Long individualId, Long grantProgramId, Integer toReservedNum) {
        Training training = trainingRepository.get(trainingId);
        Individual individual = individualRepository.get(individualId);
        GrantProgram grantProgram = grantProgramRepository.get(grantProgramId);

        //UTWORZENIE INSTANCJI SZKOLENIA
        TrainingInstance trainingInstance = new TrainingInstance();
        trainingInstance.setTraining(training);
        trainingInstance.setIndividual(individual);
        trainingInstance.setGrantProgram(grantProgram);
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(TrainingInstanceStatus.RES_CODE));
        trainingInstance.setAssignedNum(toReservedNum);
        trainingInstance.setRegisterDate(new Date());
        trainingInstance.setReimbursmentPin("44444");//TODO: tbilski
        trainingInstance = trainingInstanceRepository.save(trainingInstance);

        //POBRANIE PULI BONÓW KTÓRE MOŻNA WYKORZYSTAC
        List<PbeProductInstancePool> pools = productInstancePoolRepository.findAvaiableForUse(individualId, grantProgramId, training.getEndDate());

        //VALIDACJA
        validatePoolReservation(training, grantProgram, toReservedNum, pools);

        //UTWORZENIE WYKORZYSTANIA
        createProductInstancePoolUses(trainingInstance, pools, toReservedNum);
    }

    public void useTrainingInstance(Long trainingId, String pin){
        //TODO: tbilski sprawdzić pin

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatUse = productInstanceStatusRepository.get(PbeProductInstanceStatus.USE_CODE);
        TrainingInstanceStatus trainingInstStatUse = trainingInstanceStatusRepository.get(TrainingInstanceStatus.DONE_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance instance = trainingInstanceRepository.get(trainingId);
        instance.setStatus(trainingInstStatUse);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setUsedNum(pool.getUsedNum() + poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(PbeProductInstance i : instances){
                i.setStatus(productInstStatUse);
            }
        }
    }

    public void cancelTrainingInstance(Long trainingId){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatAssign = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGN_CODE);
        TrainingInstanceStatus trainingInstStatCancel = trainingInstanceStatusRepository.get(TrainingInstanceStatus.CANCEL_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance instance = trainingInstanceRepository.get(trainingId);
        instance.setStatus(trainingInstStatCancel);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setAvailableNum(pool.getUsedNum() + poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(PbeProductInstance i : instances){
                i.setStatus(productInstStatAssign);
                poolUse.removePollUse(i);
            }
        }
    }

    //PRIVATE METHODS

    private void createProductInstancePoolUses(TrainingInstance trainingInstance, List<PbeProductInstancePool> pools, int toReservedNum){
        PbeProductInstancePoolStatus poolStatusUse = productInstancePoolStatusRepository.get(PbeProductInstancePoolStatus.USE_CODE);
        PbeProductInstanceStatus instanceStatusRes = productInstanceStatusRepository.get(PbeProductInstanceStatus.RES_CODE);

        //TWORZENIE OBIEKTOW UZYCIA PULI BONOW
        int leftToReservedNum = toReservedNum;
        for(PbeProductInstancePool p : pools){

            //PRZEPISANIE BONOW
            int toChange = (p.getAvailableNum() > leftToReservedNum) ? leftToReservedNum : p.getAvailableNum();
            p.setAvailableNum(p.getAvailableNum() - toChange);
            p.setUsedNum(p.getUsedNum() + toChange);
            if(p.getAvailableNum() == 0){
                p.setStatus(poolStatusUse);
            }

            //UTWORZENIE OBIEKTU UZYCIA PULI BONOW
            PbeProductInstancePoolUse poolUse = createPbeProductInstancePoolUse(p, trainingInstance, toChange);
            productInstancePoolUseRepository.save(poolUse);

            //PRZYPISANIE INSTANCJI BONOW DO UZYCIA PULI BONOW
            List<PbeProductInstance> instances = productInstanceRepository.findAssignedByPool(p.getId(), toChange);
            for(PbeProductInstance i : instances){
                i.setProductInstancePoolUse(poolUse);
                i.setStatus(instanceStatusRes);
            }

            //INKREMENTACJA
            leftToReservedNum -= toChange;
            if(leftToReservedNum == 0){
                break;
            }
        }
    }

    private void validatePoolReservation(Training training, GrantProgram grantProgram, Integer toReservedNum, List<PbeProductInstancePool> pools){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        //NIE ZNALEZLISMY ZADNYCH PULI BONOW DO ROZLICZENIA
        if (pools.isEmpty()) {
            violations.add(new EntityConstraintViolation("Użytkownik nie posiada żadnej puli bonów, "
                    + "które można użyć do rezerwacji wybranego szkolenia."));
        }

        //ILOSC JAKA CHCEMY ZAREZERWOWAC PRZEKRACZA ILOSC DOSTEPNYCH BONOW W ZNALEZIONYCH PULACH
        int avaiableNum = sumAvaiableNum(pools);
        if (avaiableNum < toReservedNum) {
            violations.add(new EntityConstraintViolation("Użytkownik nie posiada odpowiedniej liczby "
                    + "ważnych bonów do rezerwacji wybranego szkolenia."));
        }

        TrainingCategoryCatalogParam tccParam = findTrainingCategoryCatalogParam(training.getCategory().getId(), grantProgram.getId(), new Date());

        //WALIDACJE PO GODZINACH SZKOLENIA (TYPOWE SZKOLENIE)
        if (tccParam.getProductInstanceForHour() != null) {
            int productInstanceForHour = tccParam.getProductInstanceForHour();

            if (training.getHoursNumber() != null) {

                //ILOSC BONÓW KTÓRĄ CHCEMY PRZEZNACZY PRZEKRACZA ILOSC BOBNÓW JAKA JEST POTRZEBNA NA WSZYSTKIE GODZINY SZKOLENIA
                //NA PRZYKLAD szkolenie IT: 1h = 2bony, szkolenie ma 10h, maksymlanie możemy przeznaczyć 20 bonów, przeznaczamy 21
                int maxToReimbursmentNum = training.getHoursNumber() * productInstanceForHour;
                if (maxToReimbursmentNum < toReservedNum) {
                    violations.add(new EntityConstraintViolation(
                            String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) "
                                    + "na rezerwację wybranego szkolenia.", toReservedNum, maxToReimbursmentNum)));
                }

                //ROZLICZAMY GODZINY CZESCIA BONOW A NIE CALOSCIA
                //NA PRZYKLAD: szklenie IT: 1h = 2bony, 2h - 4bony, 3h - 6bonów, itd
                int modToReservedNum = toReservedNum % productInstanceForHour;
                if(modToReservedNum != 0){
                    violations.add(new EntityConstraintViolation(
                            String.format("Wybrana ilość bonów nie jest zgodna z wybraną kategorią szkolenia. "
                                    + "Ilość bonów musi stanowić wielokrotność %s.", productInstanceForHour)));
                }
            }
        }

        //WALIDACJE PO CALKOWITEJ WARTOSCI (EGZAMIN)
        if (tccParam.getMaxProductInstance() != null) {
            int maxProductInstance = tccParam.getMaxProductInstance();

            //PRZEKROCZYLISMY MAKSYMALNA ILOSC BONOW DO ROZLICZENIA EGZAMINU
            //NA PRZYKLAD: maksymalnie 40 bonów, cena bonu 15PLN, maksymalnie możemy dać 600PLN
            if (maxProductInstance < toReservedNum) {
                violations.add(new EntityConstraintViolation(
                        String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) "
                                + "na rezerwację wybranego szkolenia.", toReservedNum, maxProductInstance)));
            }

            //REZERWUJEMY WIECEJ BONÓW NIŻ POTRZEBA:
            //NA PRZYKLAD: koszt egaminu 150PLN, cena bonu 15PLN, potrzeba 15bonów, rezerwujemy 16
            PbeProduct product = pools.get(0).getProduct();
            BigDecimal productValue = product.getValue();
            BigDecimal amountToReimbursment = productValue.multiply(new BigDecimal(toReservedNum));
            if(amountToReimbursment.subtract(training.getPrice()).signum() > 0){
                violations.add(new EntityConstraintViolation(
                        String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) "
                                + "na rezerwację wybranego szkolenia.", toReservedNum, maxProductInstance)));
            }
        }

        gryfValidator.validate(violations);
    }

    private TrainingCategoryCatalogParam findTrainingCategoryCatalogParam(String categoryId, Long grantProgramId, Date date){
        List<TrainingCategoryCatalogParam> params = trainingCategoryCatalogParamRepository.findByCategoryAndGrantProgramInDate(
                                                                                            categoryId,  grantProgramId, date);
        if(params.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Nie znaleziono żadnego "
                            + "parametru dla kategorii [%s], programu dofinansowania [%s] obowiązującego dnia [%s]",
                            categoryId, grantProgramId, date));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Dla danej "
                            + "dla kategorii [%s], programu dofinansowania [%s] znaleziono więcej niż jeden parametr obowiązujący dnia [%s]",
                            categoryId, grantProgramId, date));
        }
        return params.get(0);

    }

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

    private PbeProductInstancePoolUse createPbeProductInstancePoolUse(PbeProductInstancePool pool, TrainingInstance trainingInstance, int assignedNum){
        PbeProductInstancePoolUse poolUse = new PbeProductInstancePoolUse();
        poolUse.setAssignedNum(assignedNum);
        poolUse.setTrainingInstance(trainingInstance);
        poolUse.setProductInstancePool(pool);
        return poolUse;
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

    private int sumAvaiableNum(List<PbeProductInstancePool> pools){
        int sum = 0;
        for(PbeProductInstancePool p : pools){
            sum += p.getAvailableNum();
        }
        return sum;
    }
}
