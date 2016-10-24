package pl.sodexo.it.gryf.web.fo.advice.publicbenefits;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.exception.publicbenefits.PeselIndividualExistException;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumEnterpriseExistException;
import pl.sodexo.it.gryf.common.exception.publicbenefits.VatRegNumTrainingInstitutionExistException;
import pl.sodexo.it.gryf.web.fo.response.publicbenefits.PeselIndividualExistResponse;
import pl.sodexo.it.gryf.web.fo.response.publicbenefits.VatRegNumEnterpriseExistResponse;
import pl.sodexo.it.gryf.web.fo.response.publicbenefits.VatRegNumTrainingInstitutionExistResponse;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PublicBenefitExceptionHandlers {
    
    @ExceptionHandler(VatRegNumTrainingInstitutionExistException.class)
    @ResponseBody
    public ResponseEntity<VatRegNumTrainingInstitutionExistResponse> sde(VatRegNumTrainingInstitutionExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new VatRegNumTrainingInstitutionExistResponse(e.getMessage(), e.getTrainingInstitutions()));
    }

    @ExceptionHandler(VatRegNumEnterpriseExistException.class)
    @ResponseBody
    public ResponseEntity<VatRegNumEnterpriseExistResponse> sde(VatRegNumEnterpriseExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new VatRegNumEnterpriseExistResponse(e.getMessage(), e.getEnterprises()));
    }

    @ExceptionHandler(PeselIndividualExistException.class)
    @ResponseBody
    public ResponseEntity<PeselIndividualExistResponse> sde(PeselIndividualExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new PeselIndividualExistResponse(e.getMessage(), e.getIndividuals()));
    }
    
}
