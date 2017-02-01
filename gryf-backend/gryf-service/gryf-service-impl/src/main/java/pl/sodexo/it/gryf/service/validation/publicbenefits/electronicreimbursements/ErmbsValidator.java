package pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDataToValidateDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementStatus;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Walidator dla rozliczenia bonów elektornicznych
 *
 * Created by akmiecinski on 29.11.2016.
 */
@Component
public class ErmbsValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErmbsValidator.class);

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private TrainingInstanceSearchDao trainingInstanceSearchDao;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private GryfValidator gryfValidator;

    public void validateRmbs(ElctRmbsHeadDto elctRmbsHeadDto) {
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(elctRmbsHeadDto);

        validateTiReimbAccountNumber(violations, elctRmbsHeadDto);
        validateAttachments(violations, elctRmbsHeadDto);

        gryfValidator.validate(violations);
    }

    public void validateToCorrection(Ereimbursement ereimbursement) {
        List<EntityConstraintViolation> violations = new ArrayList<EntityConstraintViolation>();

        if(ereimbursement.getEreimbursementStatus().getId().equals(EreimbursementStatus.TO_CORRECT)) {
            violations.add(new EntityConstraintViolation(null, "Prośba o korektę została już wysłana wcześniej"));
        }

        gryfValidator.validate(violations);
    }

    public void validateBeforeCreateForTrainingInstance(Long trainingInstanceId){
        List<EntityConstraintViolation> violations = new ArrayList<EntityConstraintViolation>();
        TrainingInstanceDataToValidateDto trainingInstanceDataToValidateReimbursementCreation = trainingInstanceSearchDao.findTrainingInstanceDataToValidateReimbursementCreation(trainingInstanceId);

        GryfUser user = GryfUser.getLoggedUser();
        if(GryfUser.getLoggedUser() instanceof GryfTiUser){
            if(!trainingInstanceDataToValidateReimbursementCreation.getTrainingInstitutionId().equals(((GryfTiUser) user).getTrainingInstitutionId())){
                violations.add(new EntityConstraintViolation(null, "Zarezerwowane usługa, które próbujesz rozliczyć nie jest z Twojej Usługodawcy"));
            }
        }

        if(!trainingInstanceDataToValidateReimbursementCreation.isOpinionDone())
            violations.add(new EntityConstraintViolation(null, "Usługa nie zostało ocenione. Nie można utworzyć rozliczenia"));

        gryfValidator.validate(violations);
    }

    public void isCorrectStatusTransition(Long ermbsId, String nextStatus){
        List<EntityConstraintViolation> violations = new ArrayList<EntityConstraintViolation>();

        if(isNewErmbs(ermbsId)){
            validateStatusTransitionForNewErmbs(nextStatus, violations);
            gryfValidator.validate(violations);
            return;
        }
        validateStatusTransition(ermbsId, nextStatus, violations);

        gryfValidator.validate(violations);

    }

    private void validateStatusTransition(Long ermbsId, String nextStatus, List<EntityConstraintViolation> violations) {
        Ereimbursement savedEreimbursement = ereimbursementRepository.get(ermbsId);
        Collection<String> availableTransitiveStatuses = ErmbsStatusesFlowConfig.getAvailableTransitiveStatuses(savedEreimbursement.getEreimbursementStatus().getId());
        if(!availableTransitiveStatuses.contains(nextStatus)){
            violations.add(new EntityConstraintViolation(null, "Nie można zapisać rozliczenia. Niepoprawny status."));
        }
    }

    private void validateStatusTransitionForNewErmbs(String nextStatus, List<EntityConstraintViolation> violations) {
        List<String> availableStatusesForNewErmbs  = Arrays.asList(EreimbursementStatus.NEW_ERMBS, EreimbursementStatus.TO_ERMBS);
        if(!availableStatusesForNewErmbs.contains(nextStatus)){
            violations.add(new EntityConstraintViolation(null, "Nie można zapisać rozliczenia. Niepoprawny status."));
        }
    }

    private boolean isNewErmbs(Long ermbsId) {
        return ermbsId == null;
    }

    private void validateTiReimbAccountNumber(List<EntityConstraintViolation> violations, ElctRmbsHeadDto elctRmbsHeadDto){
        if (GryfStringUtils.isEmpty(elctRmbsHeadDto.getTiReimbAccountNumber())) {
            violations.add(new EntityConstraintViolation("tiReimbAccountNumber", "Konto do zwrotu środków nie może być puste", elctRmbsHeadDto.getTiReimbAccountNumber()));
            return;
        }
        IBANCheckDigit ibanCheckDigit = new IBANCheckDigit();
        if(!ibanCheckDigit.isValid("PL"+elctRmbsHeadDto.getTiReimbAccountNumber())){
            violations.add(new EntityConstraintViolation("tiReimbAccountNumber", "Konto do zwrotu środków ma niepoprawną wartość", elctRmbsHeadDto.getTiReimbAccountNumber()));
        }
    }

    private void validateAttachments(List<EntityConstraintViolation> violations, ElctRmbsHeadDto elctRmbsHeadDto){
        IntConsumer myConsumer = (index) -> {
            if (StringUtils.isEmpty(elctRmbsHeadDto.getAttachments().get(index).getDocumentNumber())) {
                String path = String.format("%s[%s].%s", "attachments", index, "documentNumber");
                violations.add(new EntityConstraintViolation(path, "Wiersz " + (index + 1) + ": brak numeru dokumentu"));
            }
            if (elctRmbsHeadDto.getAttachments().get(index).getDocumentDate() == null) {
                String path = String.format("%s[%s].%s", "attachments", index, "documentDate");
                violations.add(new EntityConstraintViolation(path, "Wiersz " + (index + 1) + ": brak daty dokumentu"));
            }
            if (!wasFileAddedBefore(elctRmbsHeadDto, index) && elctRmbsHeadDto.getAttachments().get(index).getFile() == null) {
                String path = String.format("%s[%s].%s", "attachments", index, "file");
                violations.add(new EntityConstraintViolation(path, "Wiersz " + (index + 1) + ": nie załączono pliku"));
            }
        };

        if (elctRmbsHeadDto.getAttachments() != null) {
            IntStream.range(0, elctRmbsHeadDto.getAttachments().size()).forEach(myConsumer);
        }
    }

    public void validateFileAttachments(ElctRmbsHeadDto elctRmbsHeadDto){
        List<EntityConstraintViolation> violations = new ArrayList<EntityConstraintViolation>();

        IntConsumer myConsumer = (index) -> {
            FileDTO fileDTO = elctRmbsHeadDto.getAttachments().get(index).getFile();
            if (fileDTO != null && !Strings.isNullOrEmpty(fileDTO.getOriginalFilename())) {
                String fileExtension = GryfStringUtils.findFileExtension(fileDTO.getOriginalFilename()).toLowerCase();
                Set<String> allowedFileExtensionSet = applicationParameters.getEreimbursmentAttachmentFileExtensionSet();

                if(!allowedFileExtensionSet.contains(fileExtension)){
                    String path = String.format("%s[%s].%s", "attachments", index, "file");
                    violations.add(new EntityConstraintViolation(path, "Wiersz " + (index + 1) + ": nieprawidłowy typ pliku"));
                }
            }
        };
        if (elctRmbsHeadDto.getAttachments() != null) {
            IntStream.range(0, elctRmbsHeadDto.getAttachments().size()).forEach(myConsumer);
        }
        gryfValidator.validate(violations);
    }

    private boolean wasFileAddedBefore(ElctRmbsHeadDto elctRmbsHeadDto, int index) {
        return elctRmbsHeadDto.getAttachments().get(index).getOriginalFileName() != null && !StringUtils.isEmpty(elctRmbsHeadDto.getAttachments().get(index).getOriginalFileName());
    }

    private static class ErmbsStatusesFlowConfig {

        public static Collection<String> getAvailableTransitiveStatuses(String currentStatus){
            List<String> availableTransitiveStatuses = new ArrayList<>();

            if(EreimbursementStatus.NEW_ERMBS.equals(currentStatus)){
                availableTransitiveStatuses.add(EreimbursementStatus.CANCELED);
                availableTransitiveStatuses.add(EreimbursementStatus.NEW_ERMBS);
                availableTransitiveStatuses.add(EreimbursementStatus.TO_ERMBS);
            }

            if(EreimbursementStatus.TO_ERMBS.equals(currentStatus)){
                availableTransitiveStatuses.add(EreimbursementStatus.CANCELED);
                availableTransitiveStatuses.add(EreimbursementStatus.TO_ERMBS);
                availableTransitiveStatuses.add(EreimbursementStatus.TO_CORRECT);
                availableTransitiveStatuses.add(EreimbursementStatus.GENERATED_DOCUMENTS);
                availableTransitiveStatuses.add(EreimbursementStatus.REJECTED);
            }

            if(EreimbursementStatus.TO_CORRECT.equals(currentStatus)){
                availableTransitiveStatuses.add(EreimbursementStatus.CANCELED);
                availableTransitiveStatuses.add(EreimbursementStatus.TO_CORRECT);
                availableTransitiveStatuses.add(EreimbursementStatus.TO_ERMBS);
            }

            if(EreimbursementStatus.GENERATED_DOCUMENTS.equals(currentStatus)){
                availableTransitiveStatuses.add(EreimbursementStatus.TO_VERIFY);
            }

            if(EreimbursementStatus.TO_VERIFY.equals(currentStatus)){
                availableTransitiveStatuses.add(EreimbursementStatus.SETTLED);
            }

            return availableTransitiveStatuses;

        }

    }

}
