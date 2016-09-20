package pl.sodexo.it.gryf.root.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.publicbenefits.ContactDataValidationDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitutionContact;
import pl.sodexo.it.gryf.root.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.root.service.ApplicationParametersService;
import pl.sodexo.it.gryf.root.service.dictionaries.ContactTypeService;
import pl.sodexo.it.gryf.root.service.local.ValidateService;
import pl.sodexo.it.gryf.root.service.publicbenefits.traininginstiutions.TrainingInstitutionService;

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

    //PUBLIC METHODS

    @Override
    public TrainingInstitution findTrainingInstitution(Long id) {
        return trainingInstitutionRepository.getForUpdate(id);
    }

    @Override
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution) {
        List<TrainingInstitution> trainingInstitutions = trainingInstitutionRepository.findTrainingInstitutions(trainingInstitution);
        return TrainingInstitutionSearchResultDTO.createList(trainingInstitutions);
    }

    @Override
    public TrainingInstitution createTrainingInstitution() {
        TrainingInstitution trainingInstitution = new TrainingInstitution();
        return trainingInstitution;
    }

    @Override
    public TrainingInstitution saveTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup) {
        validateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        trainingInstitution = trainingInstitutionRepository.save(trainingInstitution);

        trainingInstitution.setCode(generateCode(trainingInstitution.getId()));
        trainingInstitutionRepository.update(trainingInstitution, trainingInstitution.getId());
        return trainingInstitution;
    }

    @Override
    public void updateTrainingInstitution(TrainingInstitution trainingInstitution, boolean checkVatRegNumDup) {
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
            validateVatRegNumExist(trainingInstitutionList, trainingInstitution);
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

    private void validateVatRegNumExist(List<TrainingInstitution> trainingInstitutions, TrainingInstitution trainingInstitution){
        trainingInstitutions.remove(trainingInstitution);
        if(trainingInstitutions.size() > 0){
            throw new VatRegNumTrainingInstitutionExistException("W systemie istnieja zapisane podmioty o danym numerze NIP", trainingInstitutions);
        }
    }

}
