package pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation.TrainingReservationDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.verification.GryfIndUserVerificationException;
import pl.sodexo.it.gryf.common.utils.PeselUtils;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingReservationValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateIndUserAuthorizationData(IndUserAuthDataDto userAuthDataDto, IndividualDto individualDto) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = new ArrayList<>();

        validate(userAuthDataDto, violations);

        if(CollectionUtils.isEmpty(violations)) {
            validateAuthorizationData(userAuthDataDto.getVerificationCode(), individualDto);
        }

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    public void validateTrainingReservation(TrainingReservationDto reservationDto) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(reservationDto);

        if(reservationDto.getToReservedNum() == null || reservationDto.getToReservedNum() <= 0) {
            violations.add(new EntityConstraintViolation("toReservedNum", "Wartość pola 'Liczba rezerwowanych bonów' jest niepoprawna"));
        }

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

    private void validate(IndUserAuthDataDto userAuthDataDto, List<EntityConstraintViolation> violations) {
        if(StringUtils.isEmpty(userAuthDataDto.getPesel())) {
            violations.add(new EntityConstraintViolation("pesel", "Pole PESEL jest wymagane"));
        } else {
            if(!PeselUtils.validate(userAuthDataDto.getPesel())) {
                violations.add(new EntityConstraintViolation("pesel", "Wprowadzony numer PESEL jest niepoprawny"));
            }
        }
        if(StringUtils.isEmpty(userAuthDataDto.getVerificationCode())) {
            violations.add(new EntityConstraintViolation("verificationCode", "Pole kod weryfikacyjny jest wymagane"));
        }
    }

    private void validateAuthorizationData(String verificationCode, IndividualDto individualDto) {
        if(individualDto == null || individualDto.getVerificationCode() == null
                || !individualDto.getVerificationCode().equals(verificationCode)) {
            throw new GryfIndUserVerificationException("PESEL lub kod weryfikacyjny są niepoprawne");
        }
    }
}
