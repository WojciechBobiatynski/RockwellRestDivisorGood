package pl.sodexo.it.gryf.service.local.impl.publicbenefits.pbeproduct;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductInstancePoolSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.*;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstanceEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PbeProductInstancePoolEventBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.products.PrintNumberGenerator;

import java.math.BigDecimal;
import java.util.*;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_INTEGER_SCALE;

/**
 * Created by Isolution on 2016-12-21.
 */
@Service
@Transactional
public class PbeProductInstancePoolLocalServiceImpl implements PbeProductInstancePoolLocalService {

    //PRIVATE FIELDS - REPOSITORY

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

    @Autowired
    private PbeProductInstanceRepository productInstanceRepository;

    @Autowired
    private PbeProductInstanceStatusRepository productInstanceStatusRepository;

    @Autowired
    private PbeProductInstanceEventTypeRepository productInstanceEventTypeRepository;

    @Autowired
    private PbeProductInstancePoolUseRepository productInstancePoolUseRepository;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private PbeProductInstancePoolEventBuilder productInstancePoolEventBuilder;

    @Autowired
    private PbeProductInstanceEventBuilder productInstanceEventBuilder;

    @Autowired
    private PrintNumberGenerator printNumberGenerator;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private PbeProductInstancePoolEventTypeRepository productInstancePoolEventTypeRepository;

    @Autowired
    private ProductInstancePoolSearchDao productInstancePoolSearchDao;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    //PUBLIC METHODS

    @Override
    public void createPool(Order order){
        Contract contract = order.getContract();
        Individual individual = contract.getIndividual();
        Integer productInstanceNum = order.getVouchersNumber();
        PbeProduct pbeProduct = order.getPbeProduct();

        //STWORZENIE PULI BONÓW
        PbeProductInstancePool pool = createProductInstancePool(order, contract, individual, pbeProduct, productInstanceNum);
        pool = productInstancePoolRepository.save(pool);

        //STWORZENIE EVENTU DO PULI BONÓW
        productInstancePoolEventBuilder.saveEvent(pool, PbeProductInstancePoolEventType.ASSIGNMENT_CODE,
                                                                            order.getId(), productInstanceNum);

        //POBRANIE INSTANCJI PRODUKTOW
        List<PbeProductInstance> productInstances = findAvaiableProductInstanceByGrantProgram(pbeProduct, productInstanceNum);
        for(PbeProductInstance instance : productInstances){

            //ZMIANY NA INSTANCJACH PRODUKTOW
            assignProductInstance(instance, pool, contract);
            productInstanceRepository.update(instance, instance.getId());
        }

        //GENEROWANIE NUMEROW BONOW
        PbeProductInstance instanceFirst = productInstances.get(0);
        PbeProductInstance instanceLast = productInstances.get(productInstances.size() - 1);
        gryfPLSQLRepository.flush();
        gryfPLSQLRepository.generateInstancesPrintNumber(pbeProduct.getId(), instanceFirst.getId().getNumber(), instanceLast.getId().getNumber());
    }

    @Override
    public void reservePools(TrainingInstance trainingInstance){

        Training training = trainingInstance.getTraining();
        Individual individual = trainingInstance.getIndividual();
        GrantProgram grantProgram = trainingInstance.getGrantProgram();
        int toReservedNum = trainingInstance.getAssignedNum();

        //POBRANIE PULI BONÓW KTÓRE MOŻNA WYKORZYSTAC
        List<PbeProductInstancePool> pools = productInstancePoolRepository.findAvaiableForUse(individual.getId(),
                grantProgram.getId(), training.getStartDate(), training.getEndDate());

        //VALIDACJA
        validatePoolReservation(toReservedNum, pools);
        if(!pools.isEmpty()){
            validatePoolReservationNum(training, grantProgram, toReservedNum, pools.get(0).getProduct());
        }

        //UTWORZENIE WYKORZYSTANIA
        createProductInstancePoolUses(trainingInstance, pools, toReservedNum);
    }

    @Override
    public void lowerReservationPools(TrainingInstance instance, int newReservationNum){

        //VALIDACJA
        if(!instance.getPollUses().isEmpty()) {
            PbeProductInstancePool firstPool = instance.getPollUses().get(0).getProductInstancePool();
            validatePoolReservationNum(instance.getTraining(), instance.getGrantProgram(), newReservationNum, firstPool.getProduct());
        }

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatAssigned = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType lowerReservationEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.LWRSRVATON_CODE);
        PbeProductInstancePoolEventType lowerReservationPoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.LWRSRVATON_CODE);

        //ILE TRZEBA ZMIENIC
        int trainingInstanceToChangeNum = instance.getAssignedNum() - newReservationNum;

        //ITERACJA PO INSTANCJACH
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        poolUses = orderByExpiryDateDesc(poolUses);
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //CZY NADAL ZMIENIAMY
            if(trainingInstanceToChangeNum <= 0){
                break;
            }

            //ILE ZMIENIAMY W RAMACH UZYCIA PULI
            int poolUseToChangeNum = Math.min(poolUse.getAssignedNum(), trainingInstanceToChangeNum);

            //POOL EVENT
            productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                                        lowerReservationPoolEventType, instance.getId(), poolUseToChangeNum);

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUseToChangeNum);
            pool.setAvailableNum(pool.getAvailableNum() + poolUseToChangeNum);

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            List<PbeProductInstance> instancesToChange = new ArrayList<>(instances.subList(instances.size() - poolUseToChangeNum, instances.size()));
            for(PbeProductInstance i : instancesToChange){
                i.setStatus(productInstStatAssigned);
                poolUse.removePollUse(i);

                //EVENTY DO INSTANCJI PRODUKTOW
                productInstanceEventBuilder.saveEvent(i, lowerReservationEventType, instance.getId());
            }

            //UAKTUALNINIE
            poolUse.setAssignedNum(poolUse.getAssignedNum() - poolUseToChangeNum);
            trainingInstanceToChangeNum -= poolUseToChangeNum;
        }
    }

    @Override
    public void returnReservedPools(TrainingInstance trainingInstance){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatAssign = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType unrsrvatonEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.UNRSRVATON_CODE);
        PbeProductInstancePoolEventType unrsrvatonEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.UNRSRVATON_CODE);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = trainingInstance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setAvailableNum(pool.getAvailableNum() + poolUse.getAssignedNum());

            productInstancePoolEventBuilder.saveEvent(pool, unrsrvatonEventPoolType,
                    trainingInstance.getId(), poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(int i = instances.size() - 1; i >= 0; i--){

                PbeProductInstance ins = instances.get(i);

                //ZMIANA W INSTANCJACH
                ins.setStatus(productInstStatAssign);
                ins.setOrderId(null);
                poolUse.removePollUse(ins);
                ins.setEreimbursmentId(null);

                //EVENTY DO INSTANCJI PRODUKTOW
                productInstanceEventBuilder.saveEvent(ins, unrsrvatonEventType, trainingInstance.getId());
            }
        }
    }

    @Override
    public void usePools(TrainingInstance instance){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatUse = productInstanceStatusRepository.get(PbeProductInstanceStatus.USED_CODE);
        PbeProductInstanceEventType useEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.USE_CODE);
        PbeProductInstancePoolEventType usePoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.USE_CODE);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //POOL EVENT
            productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                                                usePoolEventType, instance.getId(), poolUse.getAssignedNum());

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setUsedNum(pool.getUsedNum() + poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(PbeProductInstance i : instances){
                i.setStatus(productInstStatUse);

                //EVENTY DO INSTANCJI PRODUKTOW
                productInstanceEventBuilder.saveEvent(i, useEventType, instance.getId());
            }
        }
    }

    @Override
    public void returnUsedPools(Ereimbursement ereimbursement){
        TrainingInstance instance = ereimbursement.getTrainingInstance();

        //POBRANIE STATUSOW
        PbeProductInstanceStatus productInstStatAssigned = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType rejectReimbursmentEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.REJECTRMB_CODE);
        PbeProductInstancePoolEventType rejectReimbursmentPoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.REJECTRMB_CODE);

        //ILE TRZEBA ZMIENIC
        int trainingInstanceToChangeNum =  instance.getAssignedNum();

        //ITERACJA PO INSTANCJACH
        List<PbeProductInstancePoolUse> poolUses = instance.getPollUses();
        poolUses = orderByExpiryDateDesc(poolUses);
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //CZY NADAL ZMIENIAMY
            if(trainingInstanceToChangeNum <= 0){
                break;
            }

            //ILE ZMIENIAMY W RAMACH UZYCIA PULI
            int poolUseToChangeNum = Math.min(poolUse.getAssignedNum(), trainingInstanceToChangeNum);

            //POOL EVENT
            productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                    rejectReimbursmentPoolEventType, ereimbursement.getId(), poolUseToChangeNum);

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setUsedNum(pool.getUsedNum() - poolUseToChangeNum);
            pool.setAvailableNum(pool.getAvailableNum() + poolUseToChangeNum);

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            List<PbeProductInstance> instancesToChange = new ArrayList<>(instances.subList(instances.size() - poolUseToChangeNum, instances.size()));
            for(PbeProductInstance i : instancesToChange){
                i.setStatus(productInstStatAssigned);
                poolUse.removePollUse(i);

                //EVENTY DO INSTANCJI PRODUKTOW
                productInstanceEventBuilder.saveEvent(i, rejectReimbursmentEventType, ereimbursement.getId());
            }

            //UAKTUALNINIE
            poolUse.setAssignedNum(poolUse.getAssignedNum() - poolUseToChangeNum);
            trainingInstanceToChangeNum -= poolUseToChangeNum;
        }
    }

    @Override
    public void reimbursPools(Ereimbursement ereimbursement){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus reimbInstStat = productInstanceStatusRepository.get(PbeProductInstanceStatus.REIMBD_CODE);
        PbeProductInstanceEventType reimbEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.REIMB_CODE);
        PbeProductInstancePoolEventType reimbEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.REIMB_CODE);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = ereimbursement.getTrainingInstance().getPollUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setUsedNum(pool.getUsedNum() - poolUse.getAssignedNum());
            pool.setRembursNum(pool.getRembursNum() + poolUse.getAssignedNum());

            productInstancePoolEventBuilder.saveEvent(pool, reimbEventPoolType,
                                                                ereimbursement.getId(), poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getPollUses();
            for(PbeProductInstance i : instances){
                i.setStatus(reimbInstStat);
                i.setEreimbursmentId(ereimbursement.getId());

                //EVENT
                productInstanceEventBuilder.saveEvent(i, reimbEventType, ereimbursement.getId());
            }
        }
    }

    @Override
    public void expirePools(Long ermbsId){
        Ereimbursement ereimbursement = ereimbursementRepository.get(ermbsId);

        //POBRANIE STATUSOW
        PbeProductInstanceStatus expireInstStat = productInstanceStatusRepository.get(PbeProductInstanceStatus.EXPIRED_CODE);
        PbeProductInstanceEventType expireEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.EXPIRATION_CODE);
        PbeProductInstancePoolEventType expireEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.EXPIRATION_CODE);

        //UAKTUALNINIE PULI
        PbeProductInstancePool pool = ereimbursement.getProductInstancePool();
        Integer num = pool.getAvailableNum();
        pool.setAvailableNum(pool.getAvailableNum() - num);
        pool.setExpiredNum(pool.getExpiredNum() + num);

        productInstancePoolEventBuilder.saveEvent(pool, expireEventPoolType,
                                                        ereimbursement.getId(), num);

        //ITERACJA PO INSTANCJACH PRODUKTU
        List<PbeProductInstance> instances = findInstancesToExpired(pool, num);
        for(PbeProductInstance i : instances){
            i.setStatus(expireInstStat);
            i.setEreimbursmentId(ereimbursement.getId());

            //EVENT
            productInstanceEventBuilder.saveEvent(i, expireEventType, ereimbursement.getId());
        }
    }

    @Override
    public void returnAvaiablePools(Ereimbursement ereimbursement){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus returnInstStat = productInstanceStatusRepository.get(PbeProductInstanceStatus.RETURNED_CODE);
        PbeProductInstanceEventType returnEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.RETURN_CODE);
        PbeProductInstancePoolEventType returnEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.RETURN_CODE);

        //UAKTUALNINIE PULI
        PbeProductInstancePool pool = ereimbursement.getProductInstancePool();
        Integer num = pool.getAvailableNum();
        pool.setAvailableNum(pool.getAvailableNum() - num);
        pool.setReturnedNum(pool.getReturnedNum() + num);

        productInstancePoolEventBuilder.saveEvent(pool, returnEventPoolType,
                ereimbursement.getId(), num);

        //ITERACJA PO INSTANCJACH PRODUKTU
        List<PbeProductInstance> instances = findInstancesToExpired(pool, num);
        for(PbeProductInstance i : instances){
            i.setStatus(returnInstStat);
            i.setEreimbursmentId(ereimbursement.getId());

            //EVENT
            productInstanceEventBuilder.saveEvent(i, returnEventType, ereimbursement.getId());
        }
    }

    @Override
    public List<PbeProductInstancePoolDto> findExpiredPoolInstances() {
        return productInstancePoolSearchDao.findExpiredPoolInstances();
    }

    //PRIVATE METHODS

    private void createProductInstancePoolUses(TrainingInstance trainingInstance, List<PbeProductInstancePool> pools, int toReservedNum){

        //POBRANIE STATUSOW
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
            productInstancePoolEventBuilder.saveEvent(p, resrvationEventPoolType,
                                                                            trainingInstance.getId(), toChange);

            //UTWORZENIE OBIEKTU UZYCIA PULI BONOW
            PbeProductInstancePoolUse poolUse = createPbeProductInstancePoolUse(p, trainingInstance, toChange);
            productInstancePoolUseRepository.save(poolUse);

            //PRZYPISANIE INSTANCJI BONOW DO UZYCIA PULI BONOW
            List<PbeProductInstance> instances = productInstanceRepository.findAssignedByPool(p.getId(), toChange);
            for(PbeProductInstance i : instances){
                i.setProductInstancePoolUse(poolUse);
                i.setStatus(resrvationInstanceStatus);

                //EVENTY DO INSTANCJI PRODUKTOW
                productInstanceEventBuilder.saveEvent(i, resrvationEventType, trainingInstance.getId());
            }

            //INKREMENTACJA
            leftToReservedNum -= toChange;
            if(leftToReservedNum == 0){
                break;
            }
        }
    }

    private void validatePoolReservation(Integer toReservedNum, List<PbeProductInstancePool> pools){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        //NIE ZNALEZLISMY ZADNYCH PULI BONOW DO ROZLICZENIA
        if (pools.isEmpty()) {
            violations.add(new EntityConstraintViolation("Użytkownik nie posiada żadnej puli bonów, "
                    + "które można użyć do rezerwacji wybranego usługi."));
        }else {

            //SPRAWDZENIE ABY UMOWY MIALY TEN SAM TYP (TO SAMO MSP)
            Contract firsContract = pools.get(0).getOrder().getContract();
            for(PbeProductInstancePool pool : pools){
                Contract contract = pool.getOrder().getContract();
                if(!Objects.equals(firsContract.getContractType(), contract.getContractType())){
                    violations.add(new EntityConstraintViolation("Użytkownik posiada różne typy umów."));
                }
                else if(ContractType.TYPE_ENT.equals(contract.getContractType().getId())){
                    if(!Objects.equals(firsContract.getEnterprise(), contract.getEnterprise())){
                        violations.add(new EntityConstraintViolation("Użytkownik posiada umowy podpisane na różne MŚP."));
                    }
                }
            }

            //ILOSC JAKA CHCEMY ZAREZERWOWAC PRZEKRACZA ILOSC DOSTEPNYCH BONOW W ZNALEZIONYCH PULACH
            int avaiableNum = sumAvaiableNum(pools);
            if (avaiableNum < toReservedNum) {
                violations.add(new EntityConstraintViolation("Użytkownik nie posiada odpowiedniej liczby " + "ważnych bonów do rezerwacji wybranego usługi."));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validatePoolReservationNum(Training training, GrantProgram grantProgram, Integer toReservedNum, PbeProduct product){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        TrainingCategoryParam tccParam = paramInDateService.findTrainingCategoryParam(training.getCategory().getId(),
                grantProgram.getId(), new Date(), true);

        //WALIDACJE PO GODZINACH SZKOLENIA (TYPOWE SZKOLENIE)
        if (tccParam.getProductInstanceForHour() != null) {
            int productInstanceForHour = tccParam.getProductInstanceForHour();

            if (training.getHoursNumber() != null) {

                //ILOSC BONÓW KTÓRĄ CHCEMY PRZEZNACZY PRZEKRACZA ILOSC BOBNÓW JAKA JEST POTRZEBNA NA WSZYSTKIE GODZINY SZKOLENIA
                //NA PRZYKLAD usługa IT: 1h = 2bony, usługa ma 10h, maksymlanie możemy przeznaczyć 20 bonów, przeznaczamy 21
                int maxToReimbursmentNum = training.getHoursNumber() * productInstanceForHour;
                if (maxToReimbursmentNum < toReservedNum) {
                    violations.add(new EntityConstraintViolation(
                            String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego usługi.", toReservedNum, maxToReimbursmentNum)));
                }

                //ROZLICZAMY GODZINY CZESCIA BONOW A NIE CALOSCIA
                //NA PRZYKLAD: szklenie IT: 1h = 2bony, 2h - 4bony, 3h - 6bonów, itd
                int modToReservedNum = toReservedNum % productInstanceForHour;
                if (modToReservedNum != 0) {
                    violations.add(new EntityConstraintViolation(
                            String.format("Wybrana ilość bonów nie jest zgodna z wybraną kategorią usługi. " + "Ilość bonów musi stanowić wielokrotność %s.", productInstanceForHour)));
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
                        String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego usługi.", toReservedNum, maxProductInstance)));
            }
            //REZERWUJEMY WIECEJ BONÓW NIŻ POTRZEBA:
            //NA PRZYKLAD: koszt egaminu 150PLN, cena bonu 15PLN, potrzeba 15bonów, rezerwujemy 16
            else {
                BigDecimal productValue = product.getValue();
                Integer maxProductInstanceCalculate = training.getPrice().divide(productValue, BIG_DECIMAL_INTEGER_SCALE, BigDecimal.ROUND_UP).intValue();
                if (maxProductInstanceCalculate < toReservedNum) {
                    violations.add(new EntityConstraintViolation(
                            String.format(" Wskazana ilość bonów (%s) przekracza maksymalną ilość bonów (%s) " + "na rezerwację wybranego usługi.", toReservedNum, maxProductInstanceCalculate)));
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
        pool.setReturnedNum(0);
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

        //GENEROWNAIE PRINT NUM - PRZENIESIONE DO PLSQL - generateInstancesPrintNumber
        //PrintNumberResultDto piPrintNumber = generatePrintNumber(instance.getProductEmission().getProduct(), contract, instance);
        //instance.setPrintNumber(piPrintNumber.getGeneratedPrintNumber());
        //instance.setCrc(piPrintNumber.getGeneratedChecksum());

        //EVENTY DO INSTANCJI PRODUKTOW
        productInstanceEventBuilder.saveEvent(instance, assignEventType, pool.getOrder().getId());
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

    private List<PbeProductInstancePoolUse> orderByExpiryDateDesc(List<PbeProductInstancePoolUse> poolUses){
        List<PbeProductInstancePoolUse> result = new ArrayList<>(poolUses);
        result.sort(new Comparator<PbeProductInstancePoolUse>() {
            @Override
            public int compare(PbeProductInstancePoolUse o1, PbeProductInstancePoolUse o2) {
                return (-1) * o1.getProductInstancePool().getExpiryDate().compareTo(o2.getProductInstancePool().getExpiryDate());
            }
        });
        return result;
    }

    private List<PbeProductInstance> findInstancesToExpired(PbeProductInstancePool pool, Integer num){
        List<PbeProductInstance> instances = productInstanceRepository.findByPoolAndStatus(pool.getId(), PbeProductInstanceStatus.ASSIGNED_CODE);
        if(instances.size() != num) {
            throw new RuntimeException("Dane są niespójne: ilosć bonów w statusie 'Przypisane (Wolne)' jest różna od ilość bonów 'Dostepnych' na puli.");
        }
        return instances;

    }
}
