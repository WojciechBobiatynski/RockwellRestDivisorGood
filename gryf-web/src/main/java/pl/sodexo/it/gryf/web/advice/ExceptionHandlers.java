/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pl.sodexo.it.gryf.exception.AuthAssertionFailureException;
import pl.sodexo.it.gryf.exception.AuthAssertionFailureFormException;
import pl.sodexo.it.gryf.exception.EntityValidationException;
import pl.sodexo.it.gryf.exception.StaleDataException;
import pl.sodexo.it.gryf.exception.publicbenefits.grantapplications.EntityValidationWithConfirmException;
import pl.sodexo.it.gryf.root.service.ApplicationParametersService;
import pl.sodexo.it.gryf.web.response.GeneralExceptionResponse;
import pl.sodexo.it.gryf.web.response.StaleDataResponse;
import pl.sodexo.it.gryf.web.response.UnauthorizedResponse;
import pl.sodexo.it.gryf.web.response.ValidationErrorResponse;
import pl.sodexo.it.gryf.web.response.publicbenefits.ValidationErrorWithConfirmResponse;

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.sodexo.it.gryf.exception.InvalidObjectIdException;

import static pl.sodexo.it.gryf.web.ViewResolverAttributes.DEFAULT_VIEW;
import static pl.sodexo.it.gryf.web.ViewResolverAttributes.MAIN_CONTENT_PARAM_NAME;
import static pl.sodexo.it.gryf.web.controller.ControllersUrls.PAGES_PREFIX;
import pl.sodexo.it.gryf.web.response.InvalidObjectIdExceptionResponse;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionHandlers {
    @Autowired
    ApplicationParametersService aps;
    
    @ExceptionHandler(StaleDataException.class)
    @ResponseBody
    public ResponseEntity<StaleDataResponse> sde(StaleDataException sde) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StaleDataResponse(sde.getId(), sde.getVersion(), sde.getModifiedUser(), sde.getModifiedTimestamp(), sde.getMessage()));
    }
    
    @ExceptionHandler(EntityValidationException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> validationException(EntityValidationException sde) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(sde.getViolations()));
    }

    @ExceptionHandler(EntityValidationWithConfirmException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorWithConfirmResponse> validationWithConfirmException(EntityValidationWithConfirmException e) {
        return ResponseEntity.badRequest().body(new ValidationErrorWithConfirmResponse(e.getViolations()));
    }

    @ExceptionHandler(AuthAssertionFailureException.class)
    @ResponseBody
    public ResponseEntity<UnauthorizedResponse> serviceAuthException(AuthAssertionFailureException aafe) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UnauthorizedResponse(aafe.getPrivilegesRequired()));
    }

    @ExceptionHandler(AuthAssertionFailureFormException.class)
    public ModelAndView formAuthException(AuthAssertionFailureFormException aafe) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_VIEW);
        modelAndView.addObject(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "error/authError.jsp");
        modelAndView.addObject("privilegesRequired", aafe.getPrivilegesRequired());
        modelAndView.addObject("cdnUrl", aps.getCdnUrl());
        return modelAndView;
    }

    @ExceptionHandler(InvalidObjectIdException.class)
    @ResponseBody
    public ResponseEntity<InvalidObjectIdExceptionResponse> invalidObjectIdException(InvalidObjectIdException ioie) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InvalidObjectIdExceptionResponse(ioie.getMessage()));
    }    
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<GeneralExceptionResponse> generalException(Exception sde) {
        Logger.getLogger(ExceptionHandlers.class.getSimpleName()).log(Level.SEVERE, sde.getMessage(), sde);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GeneralExceptionResponse(sde.getMessage()));
    }
    
}
