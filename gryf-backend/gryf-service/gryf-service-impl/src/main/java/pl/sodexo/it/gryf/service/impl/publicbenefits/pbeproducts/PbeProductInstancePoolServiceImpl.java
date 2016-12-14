package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproducts;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceStatus;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstanceEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstancePoolEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;
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
    private PbeProductInstancePoolRepository productInstancePoolRepository;

    @Autowired
    private PbeProductInstancePoolEventRepository productInstancePoolEventRepository;

    @Autowired
    private PbeProductInstanceRepository productInstanceRepository;

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
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private PbeProductInstancePoolUseRepository productInstancePoolUseRepository;

    @Autowired
    private ContractRepository contractRepository;

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
    private IndividualService individualService;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    TrainingInstanceService trainingInstanceService;

    @Autowired
    GryfAccessCodeGenerator gryfAccessCodeGenerator;

    @Autowired
    private PbeProductInstancePoolEventTypeRepository productInstancePoolEventTypeRepository;

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
        Integer productInstanceNum = order.getVouchersNumber();

        //STWORZENIE PULI BONÓW
        PbeProductInstancePool pool = createProductInstancePool(order, contract, individual, order.getPbeProduct(), productInstanceNum);
        pool = productInstancePoolRepository.save(pool);

        //STWORZENIE EVENTU DO PULI BONÓW
        PbeProductInstancePoolEvent event = productInstancePoolEventBuilder.createPbeProductInstancePoolEvent(pool,
                                                PbeProductInstancePoolEventType.ASSIGNMENT_CODE, order.getId(), productInstanceNum);
        productInstancePoolEventRepository.save(event);

        //POBRANIE INSTANCJI PRODUKTOW
        List<PbeProductInstance> productInstances = findAvaiableProductInstanceByGrantProgram(order.getPbeProduct(), productInstanceNum);
        for(PbeProductInstance instance : productInstances){

            //ZMIANY NA INSTANCJACH PRODUKTOW
            assignProductInstance(instance, pool, contract);
            productInstanceRepository.update(instance, instance.getId());
        }
    }

    @Override
    public void createTrainingInstance(TrainingReservationDto reservationDto) {
        trainingReservationValidator.validateTrainingReservation(reservationDto);

        Training training = trainingRepository.get(reservationDto.getTrainingId());
        Individual individual = individualRepository.get(reservationDto.getIndividualId());
        Contract contract = contractRepository.get(reservationDto.getContractId());
        GrantProgram grantProgram = contract.getGrantProgram();
        int toReservedNum = reservationDto.getToReservedNum();

        //VALIDACJA
        validateTrainingReservation(contract, training);

        //UTWORZENIE INSTANCJI SZKOLENIA
        TrainingInstance trainingInstance = new TrainingInstance();
        trainingInstance.setTraining(training);
        trainingInstance.setIndividual(individual);
        trainingInstance.setGrantProgram(grantProgram);
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(TrainingInstanceStatus.RES_CODE));
        trainingInstance.setAssignedNum(toReservedNum);
        trainingInstance.setRegisterDate(new Date());
        trainingInstance.setReimbursmentPin(AEScryptographer.encrypt(gryfAccessCodeGenerator.createReimbursmentPin()));
        trainingInstance = trainingInstanceRepository.save(trainingInstance);

        //POBRANIE PULI BONÓW KTÓRE MOŻNA WYKORZYSTAC
        List<PbeProductInstancePool> pools = productInstancePoolRepository.findAvaiableForUse(individual.getId(),
                                                         grantProgram.getId(), training.getStartDate(), training.getEndDate());

        //VALIDACJA
        validatePoolReservation(training, grantProgram, toReservedNum, pools);

        //UTWORZENIE WYKORZYSTANIA
        createProductInstancePoolUses(trainingInstance, pools, toReservedNum);

        //WYSŁANIE MAILA Z PINEM DO SZKOLENIA DO OSOBY FIZYCZNEJ
        trainingInstanceService.sendReimbursmentPin(trainingInstance.getId());
    }

    @Override
    public void useTrainingInstance(Long trainingId, String pin){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatUse = productInstanceStatusRepository.get(PbeProductInstanceStatus.USED_CODE);
        TrainingInstanceStatus trainingInstStatUse = trainingInstanceStatusRepository.get(TrainingInstanceStatus.DONE_CODE);
        PbeProductInstanceEventType useEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.USE_CODE);
        PbeProductInstancePoolEventType usePoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.USE_CODE);


        //UAKTUALNINIE INSTANCJI
        TrainingInstance instance = trainingInstanceRepository.get(trainingId);
        checkPin(instance, pin);
        instance.setStatus(trainingInstStatUse);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //POOL EVENT
            PbeProductInstancePoolEvent event = productInstancePoolEventBuilder.createPbeProductInstancePoolEvent(poolUse.getProductInstancePool(),
                                                                                        usePoolEventType, instance.getId(), poolUse.getAssignedNum());
            productInstancePoolEventRepository.save(event);

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setUsedNum(pool.getUsedNum() + poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(PbeProductInstance i : instances){
                i.setStatus(productInstStatUse);

                //EVENTY DO INSTANCJI PRODUKTOW
                PbeProductInstanceEvent piEvent = productInstanceEventBuilder.createPbeProductInstanceEvent(i,
                                                                                useEventType, instance.getId());
                pbeProductInstanceEventRepository.save(piEvent);
            }
        }
    }

    private void checkPin(TrainingInstance instance, String pin) {
        String trainingPin = AEScryptographer.decrypt(instance.getReimbursmentPin());
        if (!trainingPin.equals(pin)) {
            gryfValidator.validate("Pin nie jest zgodny z pinem ze szkolenia");
        }
    }

    public void cancelTrainingInstance(Long trainingId){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatAssign = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        TrainingInstanceStatus trainingInstStatCancel = trainingInstanceStatusRepository.get(TrainingInstanceStatus.CANCEL_CODE);
        PbeProductInstanceEventType unrsrvatonEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.UNRSRVATON_CODE);
        PbeProductInstancePoolEventType unrsrvatonEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.UNRSRVATON_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance instance = trainingInstanceRepository.get(trainingId);
        instance.setStatus(trainingInstStatCancel);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setAvailableNum(pool.getAvailableNum() + poolUse.getAssignedNum());

            PbeProductInstancePoolEvent event = productInstancePoolEventBuilder.createPbeProductInstancePoolEvent(pool,
                                                            unrsrvatonEventPoolType, instance.getId(), poolUse.getAssignedNum());
            productInstancePoolEventRepository.save(event);

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(int i = instances.size() - 1; i >= 0; i--){

                PbeProductInstance ins = instances.get(i);

                //ZMIANA W INSTANCJACH
                ins.setStatus(productInstStatAssign);
                ins.setOrderId(null);
                poolUse.removePollUse(ins);
                ins.setElectronicReimbursmentId(null);

                //EVENTY DO INSTANCJI PRODUKTOW
                PbeProductInstanceEvent piEvent = productInstanceEventBuilder.createPbeProductInstanceEvent(ins,
                                                                    unrsrvatonEventType, instance.getId());
                pbeProductInstanceEventRepository.save(piEvent);
            }
        }
    }

    //PRIVATE METHODS

    private void createProductInstancePoolUses(TrainingInstance trainingInstance, List<PbeProductInstancePool> pools, int toReservedNum){
        PbeProductInstanceStatus resrvationInstanceStatus = productInstanceStatusRepository.get(PbeProductInstanceStatus.RESERVED_CODE);
        PbeProductInstanceEventType resrvationEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.RESRVATION_CODE);
        PbeProductInstancePoolEventType resrvationEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.RESRVATION_CODE);


        //TWORZENIE OBIEKTOW UZYCIA PULI BONOW
        int leftToReservedNum = toReservedNum;
        for(PbeProductInstancePool p : pools){

            //PRZEPISANIE BONOW
            int toChange = (p.getAvailableNum() > leftToReservedNum) ? leftToReservedNum : p.getAvailableNum();
            p.setAvailableNum(p.getAvailableNum() - toChange);
            p.setReservedNum(p.getReservedNum() + toChange);

            //POOL EVENT
            PbeProductInstancePoolEvent event = productInstancePoolEventBuilder.createPbeProductInstancePoolEvent(p,
                                                                    resrvationEventPoolType, trainingInstance.getId(), toChange);
            productInstancePoolEventRepository.save(event);

            //UTWORZENIE OBIEKTU UZYCIA PULI BONOW
            PbeProductInstancePoolUse poolUse = createPbeProductInstancePoolUse(p, trainingInstance, toChange);
            productInstancePoolUseRepository.save(poolUse);

            //PRZYPISANIE INSTANCJI BONOW DO UZYCIA PULI BONOW
            List<PbeProductInstance> instances = productInstanceRepository.findAssignedByPool(p.getId(), toChange);
            for(PbeProductInstance i : instances){
                i.setProductInstancePoolUse(poolUse);
                i.setStatus(resrvationInstanceStatus);

                //EVENTY DO INSTANCJI PRODUKTOW
                PbeProductInstanceEvent piEvent = productInstanceEventBuilder.createPbeProductInstanceEvent(i,
                                                                        resrvationEventType, trainingInstance.getId());
                pbeProductInstanceEventRepository.save(piEvent);
            }

            //INKREMENTACJA
            leftToReservedNum -= toChange;
            if(leftToReservedNum == 0){
                break;
            }
        }
    }

    private void validateTrainingReservation(Contract contract, Training training) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(!training.isActive()){
            violations.add(new EntityConstraintViolation("Nie można zapisać użytkownika na nieaktywne szkolenie."));
        }

        //CATEGORIE SZKOLEN
        if(training.getCategory() != null){
            if(!contract.getCategories().contains(training.getCategory())){
                violations.add(new EntityConstraintViolation("Umowa zawarta przez użytkownika nie finansuje szkolenia z danej kategorii."));
            }
        }

        //CZY JUZ PRZYPISANY
        int assignedTrainingInstances = trainingInstanceRepository.countByTrainingAndIndividualNotCaceled(
                                                        training.getId(), contract.getIndividual().getId());
        if(assignedTrainingInstances > 0){
            violations.add(new EntityConstraintViolation("Rezerwacja dla użytkownika została już dokonana na dane szkolenie."));
        }

        //CZY SZKOLENIE TRWA
        if(training.getStartDate().before(new Date())){
            violations.add(new EntityConstraintViolation("Nie można zapisać użytkownika na trwające szkolenie."));
        }


        gryfValidator.validate(violations);
    }

     private void validatePoolReservation(Training training, GrantProgram grantProgram, Integer toReservedNum, List<PbeProductInstancePool> pools){
         List<EntityConstraintViolation> violations = Lists.newArrayList();

        //NIE ZNALEZLISMY ZADNYCH PULI BONOW DO ROZLICZENIA
        if (pools.isEmpty()) {
            violations.add(new EntityConstraintViolation("Użytkownik nie posiada żadnej puli bonów, "
                    + "które można użyć do rezerwacji wybranego szkolenia."));
        }else {

            //ILOSC JAKA CHCEMY ZAREZERWOWAC PRZEKRACZA ILOSC DOSTEPNYCH BONOW W ZNALEZIONYCH PULACH
            int avaiableNum = sumAvaiableNum(pools);
            if (avaiableNum < toReservedNum) {
                violations.add(new EntityConstraintViolation("Użytkownik nie posiada odpowiedniej liczby " + "ważnych bonów do rezerwacji wybranego szkolenia."));
            }

            TrainingCategoryParam tccParam = paramInDateService.findTrainingCategoryParam(training.getCategory().getId(),
                                                                                                    grantProgram.getId(), new Date(), true);

            //WALIDACJE PO GODZINACH SZKOLENIA (TYPOWE SZKOLENIE)
            if (tccParam.getProductInstanceForHour() != null) {
                int productInstanceForHour = tccParam.getProductInstanceForHour();

                if (training.getHoursNumber() != null) {

                    //ILOSC BONÓW KTÓRĄ CHCEMY PRZEZNACZY PRZEKRACZA ILOSC BOBNÓW JAKA JEST POTRZEBNA NA WSZYSTKIE GODZINY SZKOLENIA
                    //NA PRZYKLAD szkolenie IT: 1h = 2bony, szkolenie ma 10h, maksymlanie możemy przeznaczyć 20 bonów, przeznaczamy 21
                    int maxToReimbursmentNum = training.getHoursNumber() * productInstanceForHour;
                    if (maxToReimbursmentNum < toReservedNum) {
                        violations.add(new EntityConstraintViolation(
                                String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego szkolenia.", toReservedNum, maxToReimbursmentNum)));
                    }

                    //ROZLICZAMY GODZINY CZESCIA BONOW A NIE CALOSCIA
                    //NA PRZYKLAD: szklenie IT: 1h = 2bony, 2h - 4bony, 3h - 6bonów, itd
                    int modToReservedNum = toReservedNum % productInstanceForHour;
                    if (modToReservedNum != 0) {
                        violations.add(new EntityConstraintViolation(
                                String.format("Wybrana ilość bonów nie jest zgodna z wybraną kategorią szkolenia. " + "Ilość bonów musi stanowić wielokrotność %s.", productInstanceForHour)));
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
                            String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego szkolenia.", toReservedNum, maxProductInstance)));
                }
                //REZERWUJEMY WIECEJ BONÓW NIŻ POTRZEBA:
                //NA PRZYKLAD: koszt egaminu 150PLN, cena bonu 15PLN, potrzeba 15bonów, rezerwujemy 16
                else {
                    PbeProduct product = pools.get(0).getProduct();
                    BigDecimal productValue = product.getValue();
                    Integer maxProductInstanceCalculate = training.getPrice().divide(productValue, 0, BigDecimal.ROUND_UP).intValue();
                    if (maxProductInstanceCalculate < toReservedNum) {
                        violations.add(new EntityConstraintViolation(
                                String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego szkolenia.", toReservedNum, maxProductInstanceCalculate)));
                    }
                }
            }
        }
        gryfValidator.validate(violations);
    }

    private PrintNumberResultDto generatePrintNumber(PbeProduct product, Contract contract, PbeProductInstance instance){
        PrintNumberDto dto = new PrintNumberDto();
        dto.setFaceValue(product.getValue().multiply(new BigDecimal("100")).intValue());
        dto.setProductInstanceNumber(instance.getId().getNumber());
        dto.setTypeNumber(Integer.valueOf(product.getProductType()));
        dto.setValidDate(contract.getExpiryDate());
        return printNumberGenerator.generate(dto);
    }

    private PbeProductInstancePool createProductInstancePool(Order order, Contract contract, Individual individual,
                                                                PbeProduct product, Integer productInstanceNum){
        PbeProductInstancePool pool = new PbeProductInstancePool();
        pool.setStartDate(new Date());
        pool.setExpiryDate(contract.getExpiryDate());
        pool.setAllNum(productInstanceNum);
        pool.setAvailableNum(productInstanceNum);
        pool.setReservedNum(0);
        pool.setUsedNum(0);
        pool.setRembursNum(0);
        pool.setExpiredNum(0);
        pool.setIndividual(individual);
        pool.setOrder(order);
        pool.setProduct(product);
        return pool;
    }

    private PbeProductInstancePoolUse createPbeProductInstancePoolUse(PbeProductInstancePool pool, TrainingInstance trainingInstance, int assignedNum){
        PbeProductInstancePoolUse poolUse = new PbeProductInstancePoolUse();
        poolUse.setAssignedNum(assignedNum);
        poolUse.setTrainingInstance(trainingInstance);
        poolUse.setProductInstancePool(pool);
        return poolUse;
    }

    private void assignProductInstance(PbeProductInstance instance, PbeProductInstancePool pool, Contract contract){

        //POBRANIE STATUSOW I TYPOW DLA INSTANCI
        PbeProductInstanceStatus assignStatus = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType assignEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.ASSIGNMENT_CODE);

        //INSTANCE
        instance.setStatus(assignStatus);
        instance.setExpiryDate(contract.getExpiryDate());
        instance.setOrderId(pool.getOrder().getId());
        instance.setProductInstancePool(pool);

        //GENEROWNAIE PRINT NUM
        PrintNumberResultDto piPrintNumber = generatePrintNumber(instance.getProductEmission().getProduct(), contract, instance);
        instance.setPrintNumber(piPrintNumber.getGeneratedPrintNumber());
        instance.setCrc(piPrintNumber.getGeneratedChecksum());

        //EVENTY DO INSTANCJI PRODUKTOW
        PbeProductInstanceEvent piEvent = productInstanceEventBuilder.createPbeProductInstanceEvent(instance, assignEventType, pool.getOrder().getId());
        pbeProductInstanceEventRepository.save(piEvent);
    }

    private List<PbeProductInstance> findAvaiableProductInstanceByGrantProgram(PbeProduct product, Integer productInstanceNum){
        List<PbeProductInstance> productInstances = productInstanceRepository.findAvaiableByProduct(product.getId(), productInstanceNum);
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
