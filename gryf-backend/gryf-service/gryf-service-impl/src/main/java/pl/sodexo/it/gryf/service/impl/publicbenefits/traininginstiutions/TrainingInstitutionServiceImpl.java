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
import pl.sodexo.it.gryf.service.api.dictionaries.ContactTypeService;
import pl.sodexo.it.gryf.service.api.other.ApplicationParametersService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.traininginstiutions.TrainingInstitutionDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.TrainingInstitutionEntityMapper;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Service
@Transactional
public class TrainingInstitutionServiceImpl implements TrainingInstitutionService {

    //FIELDS

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ContactTypeService contactTypeService;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ApplicationParametersService applicationParametersService;

    @Autowired
    private TrainingInstitutionEntityMapper trainingInstitutionEntityMapper;

    @Autowired
    private TrainingInstitutionDtoMapper trainingInstitutionDtoMapper;

    //PUBLIC METHODS

    @Override
    public TrainingInstitutionDto findTrainingInstitution(Long id) {
        return trainingInstitutionEntityMapper.convert(trainingInstitutionRepository.getForUpdate(id));
    }

    @Override
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution) {
        List<TrainingInstitution> trainingInstitutions = trainingInstitutionRepository.findTrainingInstitutions(trainingInstitution);
        return TrainingInstitutionSearchResultDTO.createList(trainingInstitutions);
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
        List<EntityConstraintViolation> violations = validateService.generateViolation(trainingInstitution);

        //CONTACT DATA - VALIDATION
        validateContacts(trainingInstitution.getContacts(), violations);

        //VALIDATE (EXCEPTION)
        validateService.validate(violations);

        //VAT REG NUM EXIST - VALIDATION
        if(checkVatRegNumDup) {
            List<TrainingInstitution> trainingInstitutionList = trainingInstitutionRepository.findByVatRegNum(trainingInstitution.getVatRegNum());
            validateVatRegNumExist(trainingInstitutionEntityMapper.convert(trainingInstitutionList), trainingInstitution);
        }
    }

    private String generateCode(Long id){
        String prefix = applicationParametersService.getGryfTrainingInstitutionCodePrefix();
        int zeroCount = applicationParametersService.getGryfTrainingInstitutionCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d",prefix, id);
    }

    //PROTECTED METHODS

    private void validateContacts(List<TrainingInstitutionContact> contacts, List<EntityConstraintViolation> violations){
        int contactsSize = contacts.size();
        TrainingInstitutionContact[] contactTab = contacts.toArray(new TrainingInstitutionContact[contactsSize]);
        for (int i = 0; i < contactTab.length; i++) {

            ContactDataValidationDTO validContractData = contactTypeService.validateContractData(contactTab[i].getContactType(), contactTab[i].getContactData());
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
