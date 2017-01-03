package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceUseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.GryfOptimisticLockRuntimeException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceStatus;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import java.util.Date;
import java.util.List;

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

    //PUBLIC METHODS - ACTIONS

    @Override
    public void createTrainingInstance(TrainingReservationDto reservationDto) {
        validateTrainingReservation(reservationDto);

        //POBRANIE DANYCH
        Training training = trainingRepository.get(reservationDto.getTrainingId());
        Individual individual = individualRepository.get(reservationDto.getIndividualId());
        Contract contract = contractRepository.get(reservationDto.getContractId());
        int toReservedNum = reservationDto.getToReservedNum();

        //VALIDACJA
        validateTrainingVersion(training, reservationDto.getVersion());
        validateTrainingReservation(training, individual, contract);

        //UTWORZENIE INSTANCJI SZKOLENIA
        TrainingInstance trainingInstance = new TrainingInstance();
        trainingInstance.setTraining(training);
        trainingInstance.setIndividual(individual);
        trainingInstance.setGrantProgram(contract.getGrantProgram());
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(TrainingInstanceStatus.RES_CODE));
        trainingInstance.setAssignedNum(toReservedNum);
        trainingInstance.setRegisterDate(new Date());
        trainingInstance.setReimbursmentPin(AEScryptographer.encrypt(gryfAccessCodeGenerator.createReimbursmentPin()));
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
        trainingInstance.setStatus(trainingInstStatUse);

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
        pbeProductInstancePoolLocalService.returnPools(trainingInstance);
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
    public Long updateOpinionDone(String externalId, String pesel, boolean opinionDone){
        validateUpdateOpinionDone(externalId, pesel);

        List<TrainingInstance> trainnigInstances = trainingInstanceRepository.findByExternalIdAndPesel(externalId, pesel);
        validateUpdateOpinionDone(trainnigInstances, externalId, pesel);

        TrainingInstance ti = trainnigInstances.get(0);
        ti.setOpinionDone(opinionDone);
        return ti.getId();
    }

    //PRIVATE METHODS

    private void validateTrainingReservation(TrainingReservationDto reservationDto){
        trainingReservationValidator.validateTrainingReservation(reservationDto);
    }

    private void validateTrainingReservation(Training training, Individual individual, Contract contract) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        //CZY DANE W BAZIE
        if(training == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora szkolenia"));
        }
        if(individual == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora użytkownika"));
        }
        if(contract == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danego identyfikatora umowy"));
        }

        //AKTUWNE SZKOLENIE
        if(training != null && !training.isActive()){
            violations.add(new EntityConstraintViolation("Nie można zapisać użytkownika na nieaktywne szkolenie."));
        }

        //CATEGORIE SZKOLEN
        if(training != null && contract != null && training.getCategory() != null){
            if(!contract.getCategories().contains(training.getCategory())){
                violations.add(new EntityConstraintViolation("Umowa zawarta przez użytkownika nie finansuje szkolenia z danej kategorii."));
            }
        }

        //CZY JUZ PRZYPISANY
        if(training != null && individual != null) {
            int assignedTrainingInstances = trainingInstanceRepository.countByTrainingAndIndividualNotCaceled(training.getId(), individual.getId());
            if (assignedTrainingInstances > 0) {
                violations.add(new EntityConstraintViolation("Rezerwacja dla użytkownika została już dokonana na dane szkolenie."));
            }
        }

        //CZY SZKOLENIE TRWA
        if(training != null && training.getStartDate().before(new Date())){
            violations.add(new EntityConstraintViolation("Nie można zapisać użytkownika na trwające szkolenie."));
        }

        gryfValidator.validate(violations);
    }

    private void validateUseTrainingInstance(TrainingInstanceUseDto useDto){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(useDto.getId() == null){
            violations.add(new EntityConstraintViolation("Identyfikator instancji szkolenia nie może być pusty"));
        }
        if(Strings.isNullOrEmpty(useDto.getPin())){
            violations.add(new EntityConstraintViolation("Pin do potwierdzenie instancji szkolenia nie może być pusty"));
        }
        if(useDto.getVersion() == null){
            violations.add(new EntityConstraintViolation("Wersja instancji szkolenia nie może być pusty"));
        }
        gryfValidator.validate(violations);
    }

    private void validateUseTrainingInstance(TrainingInstance trainingInstance, TrainingInstanceUseDto useDto) {
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstance != null) {
            String trainingPin = AEScryptographer.decrypt(trainingInstance.getReimbursmentPin());
            if (!trainingPin.equals(useDto.getPin())) {
                violations.add(new EntityConstraintViolation("Pin nie jest zgodny z pinem ze szkolenia"));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validateTrainingInstance(Long trainingInstanceId){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstanceId == null){
            violations.add(new EntityConstraintViolation("Identyfikator instancji szkolenia nie może być pusty"));
        }
        gryfValidator.validate(violations);
    }

    private void validateTrainingInstance(TrainingInstance trainingInstance, String ... allowedStatuses){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(trainingInstance == null){
            violations.add(new EntityConstraintViolation("Nie znaleziono rekordu dla danej instancji szkolenia"));
        }
        if(trainingInstance != null && allowedStatuses != null && allowedStatuses.length > 0){
            boolean isInTable = GryfUtils.contain(allowedStatuses, new GryfUtils.Predicate<String>() {
                public boolean apply(String s) {
                    return trainingInstance.getStatus().getId().equals(s);
                }
            });
            if(!isInTable){
                violations.add(new EntityConstraintViolation("Operacja nie jest dozwolna dla danego rekordu"));
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
            throw new RuntimeException(String.format("Dla identyfikatora zewnetrznego [%s] oraz numeru pesel [%s] " + "znaleziono wiecej niż jeden rekord instancji szkolenia", externalId, pesel));
        } else if (trainnigInstances.size() == 0) {
            gryfValidator.validate(String.format("Dla identyfikatora zewnetrznego [%s] oraz numeru pesel [%s] " + "nie znaleziono rekord instancji szkolenia", externalId, pesel));
        }
    }
}