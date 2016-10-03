package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTrainingDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantOwnerAidProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementTrainingRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTraineeService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTrainingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jbentyn on 2016-09-30.
 */
@Service
public class ReimbursementTrainingServiceImpl implements ReimbursementTrainingService {

    @Autowired
    private ReimbursementTrainingRepository reimbursementTrainingRepository;

    @Autowired
    private GrantOwnerAidProductRepository grantOwnerAidProductRepository;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ReimbursementTraineeService reimbursementTraineeService;

    //PRIVATE METHODS - ORGANIZE LIST (TRAINING)

    @Override
    public void organizeTrainingList(Reimbursement reimbursement, List<ReimbursementTrainingDTO> trainingDtoList, boolean isRemoveAllowed) {
        if (trainingDtoList == null) {
            trainingDtoList = new ArrayList<>();
        }
        List<ReimbursementTrainingTempDTO> trainingTempDtoList = makeTrainingTempDtoList(reimbursement.getReimbursementTrainings(), trainingDtoList);

        for (ReimbursementTrainingTempDTO trainingTempDTO : trainingTempDtoList) {
            ReimbursementTraining training = trainingTempDTO.getTraining();
            ReimbursementTrainingDTO trainingDTO = trainingTempDTO.getTrainingDTO();

            //UPDATE OLD RECORD
            if (training != null && trainingDTO != null) {
                fillTraining(reimbursement, training, trainingDTO, isRemoveAllowed);

                //REMOVE OLD RECORD
            } else if (training != null) {
                if (isRemoveAllowed) {
                    reimbursement.removeReimbursementTraining(training);
                } else {
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie szkolenia o nazwie '%s", training.getTrainingName()));
                }

                //ADD NEW RECORD
            } else if (trainingDTO != null) {
                training = reimbursementTrainingRepository.save(new ReimbursementTraining());
                fillTraining(reimbursement, training, trainingDTO, isRemoveAllowed);
            }
        }
    }

    private void fillTraining(Reimbursement reimbursement, ReimbursementTraining training, ReimbursementTrainingDTO trainingDTO, boolean isRemoveAllowed) {
        training.setTrainingName(trainingDTO.getTrainingName());
        training.setTrainingDateFrom(trainingDTO.getTrainingDateFrom());
        training.setTrainingDateTo(trainingDTO.getTrainingDateTo());
        training.setTrainingPlace(trainingDTO.getTrainingPlace());
        training.setGrantOwnerAidProduct(trainingDTO.getGrantOwnerAidProductId() != null ? grantOwnerAidProductRepository.get(trainingDTO.getGrantOwnerAidProductId()) : null);
        training.setProductsNumber(trainingDTO.getProductsNumber());
        training.setTrainingHourGrossPrice(trainingDTO.getTrainingHourGrossPrice());
        training.setTrainingHoursTotal(trainingDTO.getTrainingHoursTotal());

        reimbursement.addReimbursementTraining(training);
        reimbursementTraineeService.organizeTraineeList(training, trainingDTO.getReimbursementTrainees(), isRemoveAllowed);
    }

    private List<ReimbursementTrainingTempDTO> makeTrainingTempDtoList(List<ReimbursementTraining> trainings, List<ReimbursementTrainingDTO> trainingDtoList) {
        List<ReimbursementTrainingTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTraining> trainingMap = GryfUtils.constructMap(trainings, new GryfUtils.MapConstructor<Long, ReimbursementTraining>() {

            public boolean isAddToMap(ReimbursementTraining input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementTraining input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTrainingDTO> trainingDtoMap = GryfUtils.constructMap(trainingDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTrainingDTO>() {

            public boolean isAddToMap(ReimbursementTrainingDTO input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementTrainingDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementTrainingDTO trainingDTO : trainingDtoList) {
            ReimbursementTrainingTempDTO temp = new ReimbursementTrainingTempDTO();
            temp.setTrainingDTO(trainingDTO);
            temp.setTraining((trainingDTO.getId() != null) ? trainingMap.get(trainingDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTraining training : trainings) {
            if (!trainingDtoMap.containsKey(training.getId())) {
                ReimbursementTrainingTempDTO temp = new ReimbursementTrainingTempDTO();
                temp.setTraining(training);
                result.add(temp);
            }
        }

        return result;
    }

}
