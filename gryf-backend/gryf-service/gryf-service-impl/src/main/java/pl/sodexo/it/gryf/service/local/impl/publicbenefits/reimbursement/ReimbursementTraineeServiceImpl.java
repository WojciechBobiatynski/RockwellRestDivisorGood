package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementTraineeRepository;
import pl.sodexo.it.gryf.model.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTraineeAttachmentService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTraineeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jbentyn on 2016-09-30.
 */
@Service
public class ReimbursementTraineeServiceImpl implements ReimbursementTraineeService {

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ReimbursementTraineeRepository reimbursementTraineeRepository;

    @Autowired
    private ReimbursementTraineeAttachmentService reimbursementTraineeAttachmentService;

    @Override
    public void organizeTraineeList(ReimbursementTraining training, List<ReimbursementTraineeDTO> traineeDtoList, boolean isRemoveAllowed) {
        if (traineeDtoList == null) {
            traineeDtoList = new ArrayList<>();
        }
        List<ReimbursementTraineeTempDTO> traineeTempDtoList = makeTraineeTempDtoList(training.getReimbursementTrainees(), traineeDtoList);

        for (ReimbursementTraineeTempDTO traineeTempDTO : traineeTempDtoList) {
            ReimbursementTrainee trainee = traineeTempDTO.getTrainee();
            ReimbursementTraineeDTO traineeDTO = traineeTempDTO.getTraineeDTO();

            //UPDATE OLD RECORD
            if (trainee != null && traineeDTO != null) {
                fillTrainee(training, trainee, traineeDTO, isRemoveAllowed);

                //REMOVE OLD RECORD
            } else if (trainee != null) {
                if (isRemoveAllowed) {
                    training.removeReimbursementTrainee(trainee);
                } else {
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie użytkownika o nazwie '%s", trainee.getTraineeName()));
                }

                //ADD NEW RECORD
            } else if (traineeDTO != null) {
                trainee = reimbursementTraineeRepository.save(new ReimbursementTrainee());
                fillTrainee(training, trainee, traineeDTO, isRemoveAllowed);
            }
        }
    }

    private void fillTrainee(ReimbursementTraining training, ReimbursementTrainee trainee, ReimbursementTraineeDTO traineeDTO, boolean isRemoveAllowed) {
        trainee.setTraineeName(traineeDTO.getTraineeName());
        trainee.setTraineeSex(traineeDTO.getTraineeSex() != null ? Sex.valueOf((String) traineeDTO.getTraineeSex().getId()) : null);

        training.addReimbursementTrainee(trainee);
        reimbursementTraineeAttachmentService.organizeTraineeAttachmentList(trainee, traineeDTO.getReimbursementTraineeAttachments(), isRemoveAllowed);
    }

    private List<ReimbursementTraineeTempDTO> makeTraineeTempDtoList(List<ReimbursementTrainee> trainees, List<ReimbursementTraineeDTO> traineeDtoList) {
        List<ReimbursementTraineeTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTrainee> traineeMap = GryfUtils.constructMap(trainees, new GryfUtils.MapConstructor<Long, ReimbursementTrainee>() {

            public boolean isAddToMap(ReimbursementTrainee input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementTrainee input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTraineeDTO> traineeDtoMap = GryfUtils.constructMap(traineeDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTraineeDTO>() {

            public boolean isAddToMap(ReimbursementTraineeDTO input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementTraineeDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementTraineeDTO traineeDTO : traineeDtoList) {
            ReimbursementTraineeTempDTO temp = new ReimbursementTraineeTempDTO();
            temp.setTraineeDTO(traineeDTO);
            temp.setTrainee((traineeDTO.getId() != null) ? traineeMap.get(traineeDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTrainee trainee : trainees) {
            if (!traineeDtoMap.containsKey(trainee.getId())) {
                ReimbursementTraineeTempDTO temp = new ReimbursementTraineeTempDTO();
                temp.setTrainee(trainee);
                result.add(temp);
            }
        }

        return result;
    }
}
