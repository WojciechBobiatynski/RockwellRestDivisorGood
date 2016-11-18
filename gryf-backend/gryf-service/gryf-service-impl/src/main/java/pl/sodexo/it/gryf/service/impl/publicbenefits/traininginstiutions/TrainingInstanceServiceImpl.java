package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualContactRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.api.ContactType;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.mapping.MailDtoCreator;

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
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    IndividualContactRepository individualContactRepository;

    @Override
    public List<TrainingInstanceDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria) {
        return trainingInstanceSearchDao.findTrainingToReimburseListByCriteria(criteria);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId) {
        return trainingInstanceSearchDao.findTrainingInstanceDetails(trainingInstanceId);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses() {
        return trainingInstanceSearchDao.findTiTrainingInstancesStatuses();
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
}
