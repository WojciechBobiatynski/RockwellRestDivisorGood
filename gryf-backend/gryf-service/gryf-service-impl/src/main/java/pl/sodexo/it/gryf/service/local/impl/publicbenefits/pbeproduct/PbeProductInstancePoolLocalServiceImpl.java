package pl.sodexo.it.gryf.service.local.impl.publicbenefits.pbeproduct;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.GenericBuilder;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.PrintNumberResultDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.*;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceExtRepository;
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
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeService;
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

    @Autowired
    TrainingInstanceExtRepository trainingInstanceExtRepository;

    @Autowired
    private TrainingCategoryProdInsCalcTypeService trainingCategoryProdInsCalcTypeService;

    //PUBLIC METHODS

    @Override
    public void createPool(Order order){
        Contract contract = order.getContract();
        Individual individual = contract.getIndividual();
        Integer productInstanceNum = order.getVouchersNumber();
        PbeProduct pbeProduct = order.getPbeProduct();

        //STWORZENIE PULI BON??W
        PbeProductInstancePool pool = createProductInstancePool(order, contract, individual, pbeProduct, productInstanceNum);
        pool = productInstancePoolRepository.save(pool);

        //STWORZENIE EVENTU DO PULI BON??W
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

        //POBRANIE PULI BON??W KT??RE MO??NA WYKORZYSTAC
        List<PbeProductInstancePool> pools = productInstancePoolRepository.findAvaiableForUse(individual.getId(),
                grantProgram.getId(), training.getStartDate(), training.getEndDate());

        //VALIDACJA
        validatePoolReservation(toReservedNum, pools);
        if(!pools.isEmpty()){
            validatePoolReservationNum(trainingInstance, toReservedNum, pools.get(0).getProduct());
        }

        //UTWORZENIE WYKORZYSTANIA
        createProductInstancePoolUses(trainingInstance, pools, toReservedNum);
    }

    @Override
    public void lowerReservationPools(TrainingInstance instance, int newReservationNum){

        //VALIDACJA
        if(!instance.getPoolUses().isEmpty()) {
            PbeProductInstancePool firstPool = instance.getPoolUses().get(0).getProductInstancePool();
            validatePoolReservationNum(instance, newReservationNum, firstPool.getProduct());
        }

        //POBRANIE STATUSOW
        PbeProductInstanceEventType lowerReservationEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.LWRSRVATON_CODE);
        PbeProductInstancePoolEventType lowerReservationPoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.LWRSRVATON_CODE);

        //ILE TRZEBA ZMIENIC
        int trainingInstanceToChangeNum = instance.getAssignedNum() - newReservationNum;

        //ITERACJA PO INSTANCJACH
        List<PbeProductInstancePoolUse> poolUses = instance.getPoolUses();
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
            removeProductInstancesFromPoolUse(instance, lowerReservationEventType, poolUse, poolUseToChangeNum);

            //UAKTUALNINIE
            poolUse.setAssignedNum(poolUse.getAssignedNum() - poolUseToChangeNum);
            trainingInstanceToChangeNum -= poolUseToChangeNum;
        }
    }

    @Override
    public void reduceUsedPools(TrainingInstance instance, int newUsedNum){

        //VALIDACJA
        if(!instance.getPoolUses().isEmpty()) {
            PbeProductInstancePool firstPool = instance.getPoolUses().get(0).getProductInstancePool();
            validatePoolReservationNum(instance, newUsedNum, firstPool.getProduct());
        }

        //POBRANIE STATUSOW
        PbeProductInstanceEventType lowerUsedEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.LWUSED_CODE);
        PbeProductInstancePoolEventType lowerUsedPoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.LWUSED_CODE);

        //ILE TRZEBA ZMIENIC
        int trainingInstanceToChangeNum = instance.getAssignedNum() - newUsedNum;

        //ITERACJA PO INSTANCJACH
        List<PbeProductInstancePoolUse> poolUses = instance.getPoolUses();
        poolUses = orderByExpiryDateDesc(poolUses);
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //CZY NADAL ZMIENIAMY
            if(trainingInstanceToChangeNum <= 0){
                break;
            }

            //ILE ZMIENIAMY W RAMACH UZYCIA PULI
            int poolUseToChangeNum = Math.min(poolUse.getAssignedNum(), trainingInstanceToChangeNum);

            //CHANGE POOL AND ADD EVENT
            reduceUsedProductInstancePool(instance.getId(), lowerUsedPoolEventType, poolUse, poolUseToChangeNum);

            //CHANGE POOL USE AND ADD EVENT
            removeProductInstancesFromPoolUse(instance, lowerUsedEventType, poolUse, poolUseToChangeNum);

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
        List<PbeProductInstancePoolUse> poolUses = trainingInstance.getPoolUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setAvailableNum(pool.getAvailableNum() + poolUse.getAssignedNum());

            productInstancePoolEventBuilder.saveEvent(pool, unrsrvatonEventPoolType,
                    trainingInstance.getId(), poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            for(int i = instances.size() - 1; i >= 0; i--){

                PbeProductInstance ins = instances.get(i);

                //ZMIANA W INSTANCJACH
                ins.setStatus(productInstStatAssign);
                poolUse.removeProductInstanceFromPoolUse(ins);
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
        List<PbeProductInstancePoolUse> poolUses = instance.getPoolUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //POOL EVENT
            productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                                                usePoolEventType, instance.getId(), poolUse.getAssignedNum());

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setReservedNum(pool.getReservedNum() - poolUse.getAssignedNum());
            pool.setUsedNum(pool.getUsedNum() + poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            changeProductInstances(instances, productInstStatUse, null, useEventType,instance.getId());
        }
    }

    @Override
    public void cancelTrainingInstanceUsedPools(TrainingInstance trainingInstance){

        PbeProductInstanceStatus productInstStatAssigned = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType cancelUseEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.CNCLUSE_CODE);
        PbeProductInstancePoolEventType cancelUsePoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.CNCLUSE_CODE);

        List<PbeProductInstancePoolUse> poolUses = trainingInstance.getPoolUses();
        for (PbeProductInstancePoolUse poolUse : poolUses) {
            reduceUsedProductInstancePool(trainingInstance.getId(), cancelUsePoolEventType, poolUse, poolUse.getAssignedNum());
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            for(int i = instances.size() - 1; i >= 0; i--){
                PbeProductInstance pbeProductInstance = instances.get(i);
                pbeProductInstance.setStatus(productInstStatAssigned);
                poolUse.removeProductInstanceFromPoolUse(pbeProductInstance);
                productInstanceEventBuilder.saveEvent(pbeProductInstance, cancelUseEventType, trainingInstance.getId());
            }
            poolUse.setAssignedNum(0);
        }

    }

    @Override
    public void rejectTrainingInstanceUsedPools(TrainingInstance trainingInstance) {

        PbeProductInstanceStatus pbeProductInstanceStatus = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);
        PbeProductInstanceEventType pbeProductInstanceEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.REJECTRMBTRA_CODE);
        PbeProductInstancePoolEventType pbeProductInstancePoolEventType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.REJECTRMBTRA_CODE);

        List<PbeProductInstancePoolUse> poolUses = new ArrayList<>(trainingInstance.getPoolUses());
        poolUses.forEach(poolUse -> {
            //POOL EVENT
            productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                    pbeProductInstancePoolEventType, trainingInstance.getId(), poolUse.getAssignedNum());

            //CHANGE POOL
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setRembursNum(pool.getRembursNum() - poolUse.getAssignedNum());
            pool.setAvailableNum(pool.getAvailableNum() + poolUse.getAssignedNum());

            //CHANGE INSTANCES
            List<PbeProductInstance> instances = new ArrayList<>(poolUse.getProductInstances());
            instances.forEach(pbeProductInstance -> {
                pbeProductInstance.setStatus(pbeProductInstanceStatus);
                poolUse.removeProductInstanceFromPoolUse(pbeProductInstance);
                productInstanceEventBuilder.saveEvent(pbeProductInstance, pbeProductInstanceEventType, trainingInstance.getId());
            });

            poolUse.setAssignedNum(0);
        });
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
        List<PbeProductInstancePoolUse> poolUses = instance.getPoolUses();
        poolUses = orderByExpiryDateDesc(poolUses);
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //CZY NADAL ZMIENIAMY
            if(trainingInstanceToChangeNum <= 0){
                break;
            }

            //ILE ZMIENIAMY W RAMACH UZYCIA PULI
            int poolUseToChangeNum = Math.min(poolUse.getAssignedNum(), trainingInstanceToChangeNum);

            //CHANGE POOL AND ADD EVENT
            reduceUsedProductInstancePool(ereimbursement.getId(), rejectReimbursmentPoolEventType, poolUse, poolUseToChangeNum);

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            List<PbeProductInstance> instancesToChange = new ArrayList<>(instances.subList(instances.size() - poolUseToChangeNum, instances.size()));
            for(PbeProductInstance i : instancesToChange){
                i.setStatus(productInstStatAssigned);
                poolUse.removeProductInstanceFromPoolUse(i);

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
        List<PbeProductInstancePoolUse> poolUses = ereimbursement.getTrainingInstance().getPoolUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setUsedNum(pool.getUsedNum() - poolUse.getAssignedNum());
            pool.setRembursNum(pool.getRembursNum() + poolUse.getAssignedNum());

            productInstancePoolEventBuilder.saveEvent(pool, reimbEventPoolType,
                                                                ereimbursement.getId(), poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            changeProductInstances(instances, reimbInstStat, ereimbursement.getId(), reimbEventType, ereimbursement.getId());
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
        changeProductInstances(instances, expireInstStat, ereimbursement.getId(), expireEventType, ereimbursement.getId());
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
        changeProductInstances(instances, returnInstStat, ereimbursement.getId(), returnEventType, ereimbursement.getId());
    }

    @Override
    public void cancelReimbursPools(TrainingInstance trainingInstance){

        //POBRANIE STATUSOW
        PbeProductInstanceStatus usedInstStat = productInstanceStatusRepository.get(PbeProductInstanceStatus.USED_CODE);
        PbeProductInstanceEventType unreimbEventType = productInstanceEventTypeRepository.get(PbeProductInstanceEventType.CNCLREIMB_CODE);
        PbeProductInstancePoolEventType unreimbEventPoolType = productInstancePoolEventTypeRepository.get(PbeProductInstancePoolEventType.CNCLREIMB_CODE);

        //ITERACJA PO UZYCIACH PULI
        List<PbeProductInstancePoolUse> poolUses = trainingInstance.getPoolUses();
        for(PbeProductInstancePoolUse poolUse : poolUses){

            //UAKTUALNINIE PULI
            PbeProductInstancePool pool = poolUse.getProductInstancePool();
            pool.setRembursNum(pool.getRembursNum() - poolUse.getAssignedNum());
            pool.setUsedNum(pool.getUsedNum() + poolUse.getAssignedNum());

            productInstancePoolEventBuilder.saveEvent(pool, unreimbEventPoolType,
                    trainingInstance.getId(), poolUse.getAssignedNum());

            //ITERACJA PO INSTANCJACH PRODUKTU
            List<PbeProductInstance> instances = poolUse.getProductInstances();
            changeProductInstances(instances, usedInstStat, null, unreimbEventType, trainingInstance.getId());
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
            violations.add(new EntityConstraintViolation("U??ytkownik nie posiada ??adnej puli bon??w, "
                    + "kt??re mo??na u??y?? do rezerwacji wybranego us??ugi."));
        }else {

            //SPRAWDZENIE ABY UMOWY MIALY TEN SAM TYP (TO SAMO MSP)
            Order orderFromPools = pools.get(0).getOrder();
            Contract firsContract = orderFromPools.getContract();

            // Walidacja, czy numer umowy by?? w pliku z BUR
            int fileLineQuantity = trainingInstanceExtRepository.countByIndOrderExternalId(orderFromPools.getExternalOrderId());
            if (fileLineQuantity == 0) {
                violations.add(new EntityConstraintViolation("Uczestnik nie dokona?? zapisu w BUR na wybrane szkolenie. Uczestnik zobowi??zany jest do uprzedniego zarezerwowania us??ugi w BUR. W razie w??tpliwo??ci prosimy o kontakt z Operatorem Finansowym."));
            }
            for(PbeProductInstancePool pool : pools){
                Contract contract = pool.getOrder().getContract();
                if(!Objects.equals(firsContract.getContractType(), contract.getContractType())){
                    violations.add(new EntityConstraintViolation("U??ytkownik posiada r????ne typy um??w."));
                }
                else if(ContractType.TYPE_ENT.equals(contract.getContractType().getId())){
                    if(!Objects.equals(firsContract.getEnterprise(), contract.getEnterprise())){
                        violations.add(new EntityConstraintViolation("U??ytkownik posiada umowy podpisane na r????ne M??P."));
                    }
                }
            }

            //ILOSC JAKA CHCEMY ZAREZERWOWAC PRZEKRACZA ILOSC DOSTEPNYCH BONOW W ZNALEZIONYCH PULACH
            int avaiableNum = sumAvaiableNum(pools);
            if (avaiableNum < toReservedNum) {
                violations.add(new EntityConstraintViolation("U??ytkownik nie posiada odpowiedniej liczby " + "wa??nych bon??w do rezerwacji wybranego us??ugi."));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validatePoolReservationNum(TrainingInstance trainingInstance, Integer toReservedNum, PbeProduct product){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        TrainingCategoryParam tccParam = findTrainingCategoryParam(trainingInstance.getTraining(), trainingInstance.getGrantProgram(), trainingInstance.getRegisterDate());

        Integer productInstanceForHour = getCalculateProdForHour(trainingInstance.getTraining(), trainingInstance.getRegisterDate());

        //WALIDACJE PO GODZINACH SZKOLENIA (TYPOWE SZKOLENIE)
        if (productInstanceForHour != null) {
            if (trainingInstance.getTraining().getHoursNumber() != null) {

                //ILOSC BON??W KT??R?? CHCEMY PRZEZNACZY PRZEKRACZA ILOSC BOBN??W JAKA JEST POTRZEBNA NA WSZYSTKIE GODZINY SZKOLENIA
                //NA PRZYKLAD us??uga IT: 1h = 2bony, us??uga ma 10h, maksymlanie mo??emy przeznaczy?? 20 bon??w, przeznaczamy 21
                int maxToReimbursmentNum = trainingInstance.getTraining().getHoursNumber() * productInstanceForHour;
                if (maxToReimbursmentNum < toReservedNum) {
                    violations.add(new EntityConstraintViolation(
                            String.format(" Wskazana ilo???? bon??w (%s) przekracza maksymaln?? ilo???? bon??w (%s) " + "na rezerwacj?? wybranego us??ugi.", toReservedNum, maxToReimbursmentNum)));
                }

                //ROZLICZAMY GODZINY CZESCIA BONOW A NIE CALOSCIA
                //NA PRZYKLAD: szklenie IT: 1h = 2bony, 2h - 4bony, 3h - 6bon??w, itd
                int modToReservedNum = toReservedNum % productInstanceForHour;
                if (modToReservedNum != 0) {
                    violations.add(new EntityConstraintViolation(
                            String.format("Wybrana ilo???? bon??w nie jest zgodna z wybran?? kategori?? us??ugi. " + "Ilo???? bon??w musi stanowi?? wielokrotno???? %s.", productInstanceForHour)));
                }
            }
        }

        //WALIDACJE PO CALKOWITEJ WARTOSCI (EGZAMIN)
        if (tccParam.getMaxProductInstance() != null) {
            int maxProductInstance = tccParam.getMaxProductInstance();

            //PRZEKROCZYLISMY MAKSYMALNA ILOSC BONOW DO ROZLICZENIA EGZAMINU
            //NA PRZYKLAD: maksymalnie 40 bon??w, cena bonu 15PLN, maksymalnie mo??emy da?? 600PLN
            if (maxProductInstance < toReservedNum) {
                violations.add(new EntityConstraintViolation(
                        String.format(" Wskazana ilo???? bon??w (%s) przekracza maksymaln?? ilo???? bon??w (%s) " + "na rezerwacj?? wybranego us??ugi.", toReservedNum, maxProductInstance)));
            }
            //REZERWUJEMY WIECEJ BON??W NI?? POTRZEBA:
            //NA PRZYKLAD: koszt egaminu 150PLN, cena bonu 15PLN, potrzeba 15bon??w, rezerwujemy 16
            else {
                BigDecimal productValue = product.getValue();
                Integer maxProductInstanceCalculate = trainingInstance.getTraining().getPrice().divide(productValue, BIG_DECIMAL_INTEGER_SCALE, BigDecimal.ROUND_UP).intValue();
                if (maxProductInstanceCalculate < toReservedNum) {
                    violations.add(new EntityConstraintViolation(
                            String.format(" Wskazana ilo???? bon??w (%s) przekracza maksymaln?? ilo???? bon??w (%s) " + "na rezerwacj?? wybranego us??ugi.", toReservedNum, maxProductInstanceCalculate)));
                }
            }
        }
        gryfValidator.validate(violations);
    }

    private TrainingCategoryParam findTrainingCategoryParam(Training training, GrantProgram grantProgram, Date reservationDate) {
        ProductCalculateDto productCalculateDto = GenericBuilder.of(ProductCalculateDto::new)
                .with(ProductCalculateDto::setCategoryId, training.getCategory().getId())
                .with(ProductCalculateDto::setGrantProgramId, grantProgram.getId())
                .with(ProductCalculateDto::setDate, reservationDate)
                .with(ProductCalculateDto::setTrainingId, training.getId())
                .with(ProductCalculateDto::setMaxParticipants, training.getMaxParticipants())
                .build();

        return paramInDateService.findTrainingCategoryParam(productCalculateDto, true);
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
            gryfValidator.validate ("Nie ma wystarczajacej liczby bon??w aby zerealizowa?? zam??wienie.");
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
            throw new RuntimeException("Dane s?? niesp??jne: ilos?? bon??w w statusie 'Przypisane (Wolne)' jest r????na od ilo???? bon??w 'Dostepnych' na puli.");
        }
        return instances;

    }

    /**
     * Wyliczenie liczby bon??w rozliczaj??cych jedn?? godzin?? us??ug szkoleniowych
     *
     * @param training szkolenie
     * @return liczba bon??w rozliczajca godzin?? szkolenia
     */
    private Integer getCalculateProdForHour(Training training, Date reservationDate) {
        ProductCalculateDto productCalculateDto = GenericBuilder.of(ProductCalculateDto::new)
                .with(ProductCalculateDto::setCategoryId, training.getCategory().getId())
                .with(ProductCalculateDto::setGrantProgramId, training.getGrantProgram().getId())
                .with(ProductCalculateDto::setDate, reservationDate)
                .with(ProductCalculateDto::setTrainingId, training.getId())
                .with(ProductCalculateDto::setMaxParticipants, training.getMaxParticipants())
                .build();
        return trainingCategoryProdInsCalcTypeService.getCalculateProductInstanceForHour(productCalculateDto);
    }

    /**
     * Zmniejszenie puli bon??w u??ytych. Pomniejszona liczba bon??w wraca do puli dost??pnych.
     *
     * @param sourceId id ??r??d??a zmian (trainingInstance, eReimbursment)
     * @param poolEventType typ zdarzenia zmieniaj??cego pul?? bon??w
     * @param poolUse encja wykorzystania puli bon??w
     * @param poolUseToChangeNum nowa liczba puli bon??w
     */
    private void reduceUsedProductInstancePool(Long sourceId, PbeProductInstancePoolEventType poolEventType, PbeProductInstancePoolUse poolUse, int poolUseToChangeNum) {

        //POOL EVENT
        productInstancePoolEventBuilder.saveEvent(poolUse.getProductInstancePool(),
                poolEventType, sourceId, poolUseToChangeNum);

        //CHANGE POOL
        PbeProductInstancePool pool = poolUse.getProductInstancePool();
        pool.setUsedNum(pool.getUsedNum() - poolUseToChangeNum);
        pool.setAvailableNum(pool.getAvailableNum() + poolUseToChangeNum);
    }

    /**
     * Zmniejszenie produkt??w wykorzystanych w danej puli bon??w.
     *
     * @param instance
     * @param eventType
     * @param poolUse
     * @param poolUseToChangeNum
     */
    private void removeProductInstancesFromPoolUse(TrainingInstance instance, PbeProductInstanceEventType eventType, PbeProductInstancePoolUse poolUse, int poolUseToChangeNum) {

        PbeProductInstanceStatus productInstStatAssigned = productInstanceStatusRepository.get(PbeProductInstanceStatus.ASSIGNED_CODE);

        //LOOP OVER PRODUCT INSTANCES
        List<PbeProductInstance> instances = poolUse.getProductInstances();
        List<PbeProductInstance> instancesToChange = new ArrayList<>(instances.subList(instances.size() - poolUseToChangeNum, instances.size()));
        for(PbeProductInstance pbeProductInstance : instancesToChange){
            //CHANGE PRODUCT INSTANCES
            pbeProductInstance.setStatus(productInstStatAssigned);
            poolUse.removeProductInstanceFromPoolUse(pbeProductInstance);

            //ADD EVENT
            productInstanceEventBuilder.saveEvent(pbeProductInstance, eventType, instance.getId());
        }
    }

    /**
     * Zmiana statusu na bonach.
     *
     * @param instanceList lista bon??w do zmiany
     * @param prdInstStatus nowy status bon??w
     * @param ereimbursementId id rozliczenia
     * @param prdInstEventType typ zdarzenia na bonach
     * @param sourceId id ??r??d??a zmiany
     */
    private void changeProductInstances(List<PbeProductInstance> instanceList, PbeProductInstanceStatus prdInstStatus, Long ereimbursementId, PbeProductInstanceEventType prdInstEventType, Long sourceId) {
        List<PbeProductInstance> instances = new ArrayList<>(instanceList);
        instances.forEach(pbeProductInstance -> {
            //CHANGE PRODUCT INSTANCES
            pbeProductInstance.setStatus(prdInstStatus);
            pbeProductInstance.setEreimbursmentId(ereimbursementId);

            //ADD EVENT
            productInstanceEventBuilder.saveEvent(pbeProductInstance, prdInstEventType, sourceId);
        });
    }
}
