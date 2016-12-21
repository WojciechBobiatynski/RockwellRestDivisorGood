package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

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
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
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
        return trainingInstanceSearchDao.findTrainingInstanceDetails(trainingInstanceId);
    }

    @Override
    @Cacheable(cacheName = "trainingInstanceStatuses")
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses() {
        return trainingInstanceSearchDao.findTiTrainingInstancesStatuses();
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

        //RESERVE POOLS
        pbeProductInstancePoolLocalService.reservePools(trainingInstance);

        //WYSŁANIE MAILA Z PINEM DO SZKOLENIA DO OSOBY FIZYCZNEJ
        sendReimbursmentPin(trainingInstance.getId());
    }

    @Override
    public void useTrainingInstance(Long trainingId, String pin){

        //POBRANIE STATUSOW
        TrainingInstanceStatus trainingInstStatUse = trainingInstanceStatusRepository.get(TrainingInstanceStatus.DONE_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingId);
        checkPin(trainingInstance, pin);
        trainingInstance.setStatus(trainingInstStatUse);

        //USE POOLS
        pbeProductInstancePoolLocalService.usePools(trainingInstance);
    }

    @Override
    public void cancelTrainingInstance(Long trainingId){

        //POBRANIE STATUSOW
        TrainingInstanceStatus trainingInstStatCancel = trainingInstanceStatusRepository.get(TrainingInstanceStatus.CANCEL_CODE);

        //UAKTUALNINIE INSTANCJI
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingId);
        trainingInstance.setStatus(trainingInstStatCancel);

        //ZWROCENIE PULI
        pbeProductInstancePoolLocalService.returnPools(trainingInstance);
    }

    @Override
    public void sendReimbursmentPin(Long trainingInstanceId) {
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        IndividualContact individualContact = individualContactRepository.findByIndividualAndContactType(trainingInstance.getIndividual().getId(), ContactType.TYPE_VER_EMAIL);
        mailService.scheduleMail(mailDtoCreator.createMailDTOForPinSend(trainingInstance, individualContact.getContactData()));
    }

    @Override
    public void resendReimbursmentPin(Long trainingInstanceId) {
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        IndividualContact individualContact = individualContactRepository.findByIndividualAndContactType(trainingInstance.getIndividual().getId(), ContactType.TYPE_VER_EMAIL);
        mailService.scheduleMail(mailDtoCreator.createMailDTOForPinResend(trainingInstance, individualContact.getContactData()));
    }

    //PRIVATE METHODS

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

    private void checkPin(TrainingInstance instance, String pin) {
        String trainingPin = AEScryptographer.decrypt(instance.getReimbursmentPin());
        if (!trainingPin.equals(pin)) {
            gryfValidator.validate("Pin nie jest zgodny z pinem ze szkolenia");
        }
    }
}