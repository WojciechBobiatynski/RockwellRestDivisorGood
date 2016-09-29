package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.dictionaries.ContactTypeValidator;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.traininginstiutions.TrainingInstitutionDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.TrainingInstitutionEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionEntityToSearchResultMapper;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Service
@Transactional
public class TrainingInstitutionServiceImpl implements TrainingInstitutionService {

    //FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ContactTypeValidator contactTypeValidator;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private TrainingInstitutionEntityMapper trainingInstitutionEntityMapper;

    @Autowired
    private TrainingInstitutionDtoMapper trainingInstitutionDtoMapper;

    @Autowired
    private TrainingInstitutionEntityToSearchResultMapper trainingInstitutionEntityToSearchResultMapper;

    //PUBLIC METHODS

    @Override
    public TrainingInstitutionDto findTrainingInstitution(Long id) {
        return trainingInstitutionEntityMapper.convert(trainingInstitutionRepository.getForUpdate(id));
    }

    @Override
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution) {
        List<TrainingInstitution> trainingInstitutions = trainingInstitutionRepository.findTrainingInstitutions(trainingInstitution);
        return trainingInstitutionEntityToSearchResultMapper.convert(trainingInstitutions);
    }

    @Override
    public TrainingInstitutionDto createTrainingInstitution() {
        return new TrainingInstitutionDto();
    }

    @Override
    public TrainingInstitutionDto saveTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup) {
        TrainingInstitution trainingInstitution = trainingInstitutionDtoMapper.convert(trainingInstitutionDto);
        validateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        trainingInstitution = trainingInstitutionRepository.save(trainingInstitution);

        trainingInstitution.setCode(generateCode(trainingInstitution.getId()));
        trainingInstitutionRepository.update(trainingInstitution, trainingInstitution.getId());
        return trainingInstitutionEntityMapper.convert(trainingInstitution);
    }

    @Override
    public void updateTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup) {
        TrainingInstitution trainingInstitution = trainingInstitutionDtoMapper.convert(trainingInstitutionDto);
        validateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        trainingInstitutionRepository.update(trainingInstitution, trainingInstitution.getId());
    }

    //PRIVATE METHODS

    private void validateTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup){

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(trainingInstitution);

        //CONTACT DATA - VALIDATION
        validateContacts(trainingInstitution.getContacts(), violations);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if(checkVatRegNumDup) {
            List<TrainingInstitution> trainingInstitutionList = trainingInstitutionRepository.findByVatRegNum(trainingInstitution.getVatRegNum());
            validateVatRegNumExist(trainingInstitutionEntityMapper.convert(trainingInstitutionList), trainingInstitution);
        }
    }

    private String generateCode(Long id){
        String prefix = applicationParameters.getGryfTrainingInstitutionCodePrefix();
        int zeroCount = applicationParameters.getGryfTrainingInstitutionCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d",prefix, id);
    }

    //PROTECTED METHODS

    private void validateContacts(List<TrainingInstitutionContact> contacts, List<EntityConstraintViolation> violations){
        int contactsSize = contacts.size();
        TrainingInstitutionContact[] contactTab = contacts.toArray(new TrainingInstitutionContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeValidator.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
            if(!validContractData.isValid()){

                String path = String.format("%s[%s].%s", Enterprise.CONTACTS_ATTR_NAME, i, EnterpriseContact.CONTACT_DATA_ATTR_NAME);
                violations.add(new EntityConstraintViolation(path, validContractData.getMessage(), contactTab[i].getContactData()));
            }
        }
    }

    private void validateVatRegNumExist(List<TrainingInstitutionDto> trainingInstitutions, TrainingInstitution trainingInstitution){
        trainingInstitutions.removeIf(dto ->trainingInstitution.getId().equals(dto.getId()));
        if(trainingInstitutions.size() > 0){
            throw new VatRegNumTrainingInstitutionExistException("W systemie istnieja zapisane podmioty o danym numerze NIP", trainingInstitutions);
        }
    }

}
