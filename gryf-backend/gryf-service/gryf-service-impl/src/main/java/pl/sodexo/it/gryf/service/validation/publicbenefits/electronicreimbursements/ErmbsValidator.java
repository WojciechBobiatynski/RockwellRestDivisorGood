package pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;
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
    private GryfValidator gryfValidator;

    public void validateRmbs(ElctRmbsHeadDto elctRmbsHeadDto) {
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(elctRmbsHeadDto);

        validateTiReimbAccountNumber(violations, elctRmbsHeadDto);
        validateAttachments(violations, elctRmbsHeadDto);

        gryfValidator.validate(violations);
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
        //TODO wynieść stringi do stałych
        IntConsumer myConsumer = (index) -> {
            if (elctRmbsHeadDto.getAttachments().get(index).getDocumentNumber() == null || StringUtils.isEmpty(elctRmbsHeadDto.getAttachments().get(index).getDocumentNumber())) {
                String path = String.format("%s[%s].%s", "attachments", index, "documentNumber");
                violations.add(new EntityConstraintViolation(path, "Brak numeru dokumentu"));
            }
            if (!wasFileAddedBefore(elctRmbsHeadDto, index) && elctRmbsHeadDto.getAttachments().get(index).getFile() == null) {
                String path = String.format("%s[%s].%s", "attachments", index, "file");
                violations.add(new EntityConstraintViolation(path, "Nie załączono pliku"));
            }
        };

        if (elctRmbsHeadDto.getAttachments() != null) {
            IntStream.range(0, elctRmbsHeadDto.getAttachments().size()).forEach(myConsumer);
        }
    }

    private boolean wasFileAddedBefore(ElctRmbsHeadDto elctRmbsHeadDto, int index) {
        return elctRmbsHeadDto.getAttachments().get(index).getOriginalFileName() != null && !StringUtils.isEmpty(elctRmbsHeadDto.getAttachments().get(index).getOriginalFileName());
    }
}
