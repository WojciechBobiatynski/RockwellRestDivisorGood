package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.GenericBuilder;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceUseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-10-26.
 */
@Service
@Transactional
public class TrainingInstanceServiceImpl implements TrainingInstanceService {

    @Autowired
    private TrainingInstanceSearchDao trainingInstanceSearchDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailDtoCreator mailDtoCreator;

    @Autowired
    IndividualContactRepository individualContactRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PbeProductInstancePoolLocalService pbeProductInstancePoolLocalService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private TrainingReservationValidator trainingReservationValidator;

    @Autowired
    private GryfAccessCodeGenerator gryfAccessCodeGenerator;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TrainingCategoryProdInsCalcTypeService trainingCategoryProdInsCalcTypeService;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    //PUBLIC METHODS

    @Override
    public List<TrainingInstanceDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria) {
        return trainingInstanceSearchDao.findTrainingToReimburseListByCriteria(criteria);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId) {
        validateTrainingInstance(trainingInstanceId);
        return trainingInstanceSearchDao.findTrainingInstanceDetails(trainingInstanceId);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetailsWithPinCode(Long trainingInstanceId) {
        validateTrainingInstance(trainingInstanceId);
        TrainingInstanceDetailsDto foundedTrainingInstanceDetailsDto = trainingInstanceSearchDao.findTrainingInstanceDetailsWithPinCode(trainingInstanceId);
        foundedTrainingInstanceDetailsDto.setPinCode(AEScryptographer.decrypt(foundedTrainingInstanceDetailsDto.getPinCode()));
        return foundedTrainingInstanceDetailsDto;
    }

    @Override
    @Cacheable(cacheName = "trainingInstanceStatuses")
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses() {
        return trainingInstanceSearchDao.findTiTrainingInstancesStatuses();
    }

    @Override
    public boolean isTrainingInstanceInLoggedUserInstitution(Long trainingInstanceId){
        return trainingInstanceRepository.isInUserInstitution(trainingInstanceId, GryfUser.getLoggedUserLogin());
    }

    @Override
    public boolean isTrainingInstanceInLoggedIndividual(Long trainingInstanceId){
        return trainingInstanceRepository.isInUserIndividual(trainingInstanceId, GryfUser.getLoggedUserLogin());
    }

    @Override
    public Date findReservationDatePossibility(Long grantProgramId, Date date){
        int reservationDayNumPossibility = findReservationDayNumPossibility(grantProgramId);
        return findReservationDatePossibility(date, reservationDayNumPossibility);
    }

    //PUBLIC METHODS - ACTIONS

    @Override
    public void createTrainingInstance(TrainingReservationDto trainingReservationDto) {
        //wybranie
        GrantProgramDictionaryDTO grantProgramDictionaryDTO = GenericBuilder.of(GrantProgramDictionaryDTO::new)
                .with(GrantProgramDictionaryDTO::setId, trainingReservationDto.getGrantProgramId()).build();
        Contract contract = contractRepository.findContractIndividualByProgramAndDate(grantProgramDictionaryDTO, trainingReservationDto.getIndividualId(), trainingReservationDto.getStartDate(), trainingReservationDto.getEndDate() );
        trainingReservationDto.setContractId(contract.getId());

        if (!Objects.isNull(contract)) {
            // Znaleziono kontrakt
            trainingReservationDto.setContractId(contract.getId());
        }

        //walidacja danych
        validateTrainingReservation(trainingReservationDto);

        //POBRANIE DANYCH
        Training training = trainingRepository.get(trainingReservationDto.getTrainingId());
        Individual individual = individualRepository.get(trainingReservationDto.getIndividualId());
        int toReservedNum = trainingReservationDto.getToReservedNum();
        String verificationCode = trainingReservationDto.getVerificationCode();

        //VALIDACJA
        validateTrainingVersion(training, trainingReservationDto.getVersion());
        validateTrainingReservation(individual, verificationCode);
        validateTrainingReservation(training, individual, contract, verificationCode);

        //UTWORZENIE INSTANCJI SZKOLENIA
        TrainingInstance trainingInstance = new TrainingInstance();
        trainingInstance.setTraining(training);
        trainingInstance.setIndividual(individual);
        trainingInstance.setGrantProgram(contract.getGrantProgram());
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(TrainingInstanceStatus.RES_CODE));
        trainingInstance.setAssignedNum(toReservedNum);
        trainingInstance.setRegisterDate(new Date());
        trainingInstance.setReimbursmentPin(AEScryptographer.encrypt(gryfAccessCodeGenerator.createReimbursmentPin()));
        trainingInstance.setProductInstanceCalcForHour(getProductInstanceForHour(trainingReservationDto));
        trainingInstance = trainingInstanceRepository.save(trainingInstance);

        //RESERVE POOLS
        pbeProductInstancePoolLocalService.reservePools(trainingInstance);

        //WYSŁANIE MAILA Z PINEM DO SZKOLENIA DO OSOBY FIZYCZNEJ
        sendReimbursmentPin(trainingInstance.getId());
    }

    @Override
    public void useTrainingInstance(TrainingInstanceUseDto useDto){
        validateUseTrainingInstance(useDto);

        //POBRANIE DANYCH
        TrainingInstanceStatus trainingInstStatUse = trainingInstanceStatusRepository.get(TrainingInstanceStatus.DONE_CODE);
        TrainingInstance trainingInstance = trainingInstanceRepository.get(useDto.getId());

        //AKCJE
        validateTrainingInstanceVersion(trainingInstance, useDto.getVersion());
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.RES_CODE);
        validateUseTrainingInstance(trainingInstance, useDto);
        validateReduceProductNumTrainingInstance(trainingInstance, useDto);
        trainingInstance.setStatus(trainingInstStatUse);

        //LOWER RESERVATION POOLS
        if(useDto.getNewReservationNum() < trainingInstance.getAssignedNum()){
            pbeProductInstancePoolLocalService.lowerReservationPools(trainingInstance, useDto.getNewReservationNum());
            trainingInstance.setAssignedNum(useDto.getNewReservationNum());
        }

        //USE POOLS
        pbeProductInstancePoolLocalService.usePools(trainingInstance);
    }

    @Override
    public void cancelTrainingInstance(Long trainingInstanceId, Integer version){
        validateTrainingInstance(trainingInstanceId);

        //POBRANIE STATUSOW
        TrainingInstanceStatus trainingInstStatCancel = trainingInstanceStatusRepository.get(TrainingInstanceStatus.CANCEL_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        validateTrainingInstanceVersion(trainingInstance, version);
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.RES_CODE);
        trainingInstance.setStatus(trainingInstStatCancel);

        //ZWROCENIE PULI
        pbeProductInstancePoolLocalService.returnReservedPools(trainingInstance);
    }

    @Override
    public void sendReimbursmentPin(Long trainingInstanceId) {
        validateTrainingInstance(trainingInstanceId);

        //WYSLANIE MAILA
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        validateTrainingInstance(trainingInstance);
        IndividualContact individualContact = individualContactRepository.findByIndividualAndContactType(trainingInstance.getIndividual().getId(), ContactType.TYPE_VER_EMAIL);
        mailService.scheduleMail(mailDtoCreator.createMailDTOForPinSend(trainingInstance, individualContact.getContactData()));
    }

    @Override
    public void resendReimbursmentPin(Long trainingInstanceId) {
        validateTrainingInstance(trainingInstanceId);

        //WYSLANIE MAILA
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        validateTrainingInstance(trainingInstance);
        IndividualContact individualContact = individualContactRepository.findByIndividualAndContactType(trainingInstance.getIndividual().getId(), ContactType.TYPE_VER_EMAIL);
        mailService.scheduleMail(mailDtoCreator.createMailDTOForPinResend(trainingInstance, individualContact.getContactData()));
    }

    @Override
    public void reduceProductAssignedNum(TrainingInstanceUseDto useDto) {
        validateTrainingInstance(useDto.getId());

        //GET DATA
        TrainingInstance trainingInstance = trainingInstanceRepository.get(useDto.getId());

        //VALIDATE
        validateTrainingInstanceVersion(trainingInstance, useDto.getVersion());
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.DONE_CODE);
        validateReduceProductNumTrainingInstance(trainingInstance, useDto);

        //REDUCE PRODUCT NUM
        pbeProductInstancePoolLocalService.reduceUsedPools(trainingInstance, useDto.getNewReservationNum());
        trainingInstance.setAssignedNum(useDto.getNewReservationNum());
    }

    @Override
    public void cancelTrainingReimbursement(Long trainingInstanceId, Integer version) {
        validateTrainingInstance(trainingInstanceId);

        //GET DATA
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        ElctRmbsHeadDto elctRmbsHeadDto = electronicReimbursementsDao.findEcltRmbsByTrainingInstanceId(trainingInstanceId);

        //VALIDATE
        validateTrainingInstanceVersion(trainingInstance, version);
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.REIMB_CODE);

        //SET TRAINING DONE
        electronicReimbursementsService.cancel(elctRmbsHeadDto.getErmbsId());
        pbeProductInstancePoolLocalService.cancelReimbursPools(trainingInstance);
    }

    @Override
    public void cancelTrainingInstanceDone(Long trainingInstanceId, Integer version){
        validateTrainingInstance(trainingInstanceId);

        //GET DATA
        TrainingInstanceStatus trainingInstStatCancel = trainingInstanceStatusRepository.get(TrainingInstanceStatus.CANCEL_CODE);
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);

        //VALIDATE
        validateTrainingInstanceVersion(trainingInstance, version);
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.DONE_CODE);

        //SET CANCEL
        trainingInstance.setStatus(trainingInstStatCancel);
        pbeProductInstancePoolLocalService.cancelTrainingInstanceUsedPools(trainingInstance);
    }

    @Override
    public void rejectTrainingInstanceReimb(Long trainingInstanceId, Integer version) {
        validateTrainingInstance(trainingInstanceId);

        //GET DATA
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        ElctRmbsHeadDto elctRmbsHeadDto = electronicReimbursementsDao.findEcltRmbsByTrainingInstanceId(trainingInstanceId);

        //VALIDATE
        validateTrainingInstanceVersion(trainingInstance, version);
        validateTrainingInstance(trainingInstance, TrainingInstanceStatus.REIMB_CODE);

        //REJECT RMB AND TRAINING
        electronicReimbursementsService.rejectForReimbursementTraining(elctRmbsHeadDto.getErmbsId());
        pbeProductInstancePoolLocalService.rejectTrainingInstanceUsedPools(trainingInstance);
    }

    @Override
    public Long updateOpinionDone(String externalId, String pesel, boolean opinionDone){
        validateUpdateOpinionDone(externalId, pesel);

        List<TrainingInstance> trainnigInstances = trainingInstanceRepository.findByExternalIdAndPesel(externalId, pesel,
                Collections.singletonList(TrainingInstanceStatus.CANCEL_CODE));
        validateUpdateOpinionDone(trainnigInstances, externalId, pesel);

        TrainingInstance ti = trainnigInstances.get(0);
        ti.setOpinionDone(opinionDone);
        return ti.getId();
    }

    //PRIVATE METHODS - VALIDATE

    private void validateTrainingReservation(TrainingReservationDto reservationDto){
        trainingReservationValidator.validateTrainingReservation(reservationDto);
    }

    private void validateTrainingReservation(Training training, Individual individual, Contract contract, String verificationCode) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        //CZY DANE W BAZIE
        if(training == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora usługi"));
        }
        if(individual == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora użytkownika"));
        }
        if(contract == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora umowy"));
        }

        //AKTUWNE SZKOLENIE
        if(training != null && !training.isActive()){
            violations.add(new EntityConstraintViolation("Nie można zapisać użytkownika na nieaktywne usługa."));
        }

        //CATEGORIE SZKOLEN
        if(training != null && contract != null && training.getCategory() != null){
            if(!contract.getCategories().contains(training.getCategory())){
                violations.add(new EntityConstraintViolation("Umowa zawarta przez użytkownika nie finansuje usługi z danej kategorii."));
            }
        }

        //CZY JUZ PRZYPISANY
        if(training != null && individual != null) {
            int assignedTrainingInstances = trainingInstanceRepository.countByTrainingAndIndividualNotCaceled(training.getId(), individual.getId());
            if (assignedTrainingInstances > 0) {
                violations.add(new EntityConstraintViolation("Rezerwacja dla użytkownika została już dokonana na dane usługa."));
            }
        }

        //CZY SZKOLENIE MOZNA ZAREZERWOWAC ZE WZGLEDU NA DATE ROZPOCZECIA
        if(training != null && contract != null){
            int reservationDayNumPossibility = findReservationDayNumPossibility(contract.getGrantProgram().getId());
            Date reservationDatePossibility = findReservationDatePossibility(new Date(), reservationDayNumPossibility);

            if(training.getStartDate().before(reservationDatePossibility)) {
                violations.add(new EntityConstraintViolation(getReservationDatePossibilityMessage(reservationDayNumPossibility)));
            }
        }

        gryfValidator.validate(violations);
    }

    private void validateTrainingReservation(Individual individual, String verificationCode) {
        if(individual != null) {
            trainingReservationValidator.validateAuthorizationData(individual, verificationCode);
        }
    }

    private void validateUseTrainingInstance(TrainingInstanceUseDto useDto){
        gryfValidator.validate(useDto);
    }

    private void validateUseTrainingInstance(TrainingInstance trainingInstance, TrainingInstanceUseDto useDto) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstance != null) {
            String trainingPin = AEScryptographer.decrypt(trainingInstance.getReimbursmentPin());
            if (!trainingPin.equals(useDto.getPin())) {
                violations.add(new EntityConstraintViolation("Pin nie jest zgodny z pinem ze usługi"));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validateReduceProductNumTrainingInstance(TrainingInstance trainingInstance, TrainingInstanceUseDto useDto) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstance != null) {
            if(trainingInstance.getAssignedNum() < useDto.getNewReservationNum()){
                violations.add(new EntityConstraintViolation("Nie można zwiększyć ilości zarezerwowanych bonów (możliwe jest tylko zmniejszenie)."));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validateTrainingInstance(Long trainingInstanceId){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstanceId == null){
            violations.add(new EntityConstraintViolation("Identyfikator instancji usługi nie może być pusty"));
        }
        gryfValidator.validate(violations);
    }

    private void validateTrainingInstance(TrainingInstance trainingInstance, String ... allowedStatuses){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstance == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danej instancji usługi"));
        }
        if(trainingInstance != null && allowedStatuses != null && allowedStatuses.length > 0){
            boolean isInTable = GryfUtils.contain(allowedStatuses, new GryfUtils.Predicate<String>() {
                public boolean apply(String s) {
                    return trainingInstance.getStatus().getId().equals(s);
                }
            });
            if(!isInTable){
                violations.add(new EntityConstraintViolation("Operacja nie jest dozwolona dla danego rekordu"));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validateTrainingInstanceVersion(TrainingInstance trainingInstance, Integer version){
        if(!trainingInstance.getVersion().equals(version)){
            throw new GryfOptimisticLockRuntimeException();
        }
    }

    private void validateTrainingVersion(Training training, Integer version){
        if(!training.getVersion().equals(version)){
            throw new GryfOptimisticLockRuntimeException();
        }
    }

    private void validateUpdateOpinionDone(String externalId, String pesel){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(Strings.isNullOrEmpty(externalId)){
            violations.add(new EntityConstraintViolation("Identyfikator zewnętrzny nie moze być pusty"));
        }
        if(Strings.isNullOrEmpty(pesel)){
            violations.add(new EntityConstraintViolation("Pesel nie moze być pusty"));
        }

        gryfValidator.validate(violations);
    }

    private void validateUpdateOpinionDone(List<TrainingInstance> trainnigInstances, String externalId, String pesel) {
        if (trainnigInstances.size() > 1) {
            throw new RuntimeException(String.format("Dla identyfikatora zewnetrznego [%s] oraz numeru pesel [%s] " + "znaleziono wiecej niż jeden rekord instancji usługi", externalId, pesel));
        } else if (trainnigInstances.size() == 0) {
            gryfValidator.validate(String.format("Dla identyfikatora zewnetrznego [%s] oraz numeru pesel [%s] " + "nie znaleziono rekord instancji usługi", externalId, pesel));
        }
    }

    //PRIVATE METHODS - RESERVATION DATE POSSIBILITY

    private int findReservationDayNumPossibility(Long grantProgramId){
        GrantProgramParam rdnpParam = paramInDateService.findGrantProgramParam(grantProgramId, GrantProgramParam.RESERVATION_DAY_NUM_POSSIBILITY, new Date(), false);
        if(rdnpParam != null){
            try{
                return Integer.valueOf(rdnpParam.getValue());
            }catch(NumberFormatException e){
                return 0;
            }
        }
        return 0;
    }

    private Date findReservationDatePossibility(Date date, int reservationDayNumPossibility){
        return  GryfUtils.getStartDay(GryfUtils.addDays(date, reservationDayNumPossibility));
    }

    private String getReservationDatePossibilityMessage(int reservationDayNum){
        if(reservationDayNum == 0){
            return "Dane szkolenie jest rozpoczęte. Nie można zapisać użytkownika na trwające usługa.";
        }else if (reservationDayNum == -1){
            return String.format("Użytkownika można zapisać na szkolenie najpóźniej do %s dnia po rozpoczęciu", Math.abs(reservationDayNum));
        }else if (reservationDayNum < -1){
            return String.format("Użytkownika można zapisać na szkolenie najpóźniej do %s dni po rozpoczęciu", Math.abs(reservationDayNum));
        }else if (reservationDayNum == 1){
            return String.format("Użytkownika można zapisać na szkolenie najpóźniej do %s dnia przed rozpoczęciem", Math.abs(reservationDayNum));
        }else {
            return String.format("Użytkownika można zapisać na szkolenie najpóźniej do %s dni przed rozpoczęciem", Math.abs(reservationDayNum));
        }
    }

    /**
     * Wyliczenie liczby bonów rozliczających jedną godzinę usług szkoleniowych
     *
     * @param trainingReservationDto dane potrzebne do rezerwacji szkolenia
     * @return liczba bonów rozliczajca godzinę szkolenia
     */
    private Integer getProductInstanceForHour(TrainingReservationDto trainingReservationDto) {
        Training training = trainingRepository.get(trainingReservationDto.getTrainingId());
        ProductCalculateDto productCalculateDto = GenericBuilder.of(ProductCalculateDto::new)
                .with(ProductCalculateDto::setCategoryId, training.getCategory().getId())
                .with(ProductCalculateDto::setGrantProgramId, trainingReservationDto.getGrantProgramId())
                .with(ProductCalculateDto::setDate, trainingReservationDto.getStartDate())
                .with(ProductCalculateDto::setTrainingId, trainingReservationDto.getTrainingId())
                .with(ProductCalculateDto::setIndividualTraining, training.isIndividual())
                .build();
        return trainingCategoryProdInsCalcTypeService.getCalculateProductInstanceForHour(productCalculateDto);
    }
}