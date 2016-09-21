package pl.sodexo.it.gryf.root.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.FileContainerDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.exception.publicbenefits.grantapplications.EntityValidationWithConfirmException;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.root.service.ApplicationParametersService;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.local.ValidateService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomasz.Bilski on 2015-06-10.
 */
@Service
public class ValidateServiceImpl implements ValidateService {

    //PRIVATE FIELDS

    @Autowired
    private ApplicationParametersService parameterService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private Validator validator;

    //PUBLIC METHODS - GENERATE VIOLATION

    @Override
    public  List<EntityConstraintViolation> generateViolation(Object o, Class<?>... groups) {
        List<EntityConstraintViolation> violations = new ArrayList<>();

        //JAVAX VALIDATION
        Set<ConstraintViolation<Object>> entityViolations = validator.validate(o, groups);
        for (ConstraintViolation<Object> v : entityViolations) {
            violations.add(new EntityConstraintViolation(StringUtils.toString(v.getPropertyPath()), v.getMessage(), v.getInvalidValue()));
        }

        return violations;
    }

    @Override
    public void addInsertablePrivilege(List<EntityConstraintViolation> violations, Object o){
        //SECURITY VALIDATION
        for (Field field : o.getClass().getDeclaredFields()) {
            if(!securityCheckerService.hasInsertablePrivilege(field)){
                Object value = getValue(field, o);
                if((value instanceof String && !StringUtils.isEmpty((String)value))
                        || (!(value instanceof String) && value != null)) {
                    String message = securityCheckerService.getInsertablePrivilegeMessage(field);
                    violations.add(new EntityConstraintViolation(StringUtils.toString(field.getName()), message, value));
                }
            }
        }
    }

    @Override
    public void addFileMaxSizePrivilege(List<EntityConstraintViolation> violations, String prefix, List<? extends FileContainerDTO> filesContainers){
        if(filesContainers != null) {
            int attachmentMaxSize = parameterService.getAttachmentMaxSize();
            int attachmentMaxSizeMB = parameterService.getAttachmentMaxSizeMB();

            FileContainerDTO[] attachmentTab = filesContainers.toArray(new FileContainerDTO[filesContainers.size()]);
            for (int i = 0; i < attachmentTab.length; i++) {
                FileDTO fileDTO = attachmentTab[i].getFile();
                if (fileDTO != null) {
                    if (fileDTO.getSize() > attachmentMaxSize) {
                        String name = !StringUtils.isEmpty(fileDTO.getAttachmentName()) ? fileDTO.getAttachmentName() : fileDTO.getName();
                        String message = String.format("Plik dla załącznika '%s' jest zbyt duży - maksymalna wielkość załącznika to %sMB", name, attachmentMaxSizeMB);
                        violations.add(new EntityConstraintViolation(prefix + "[" + i + "].file", message, null));
                    }
                }
            }
        }
    }

    //PUBLIC METHODS - CLASSIFY BY PATH

    @Override
    public void classifyByPath(List<EntityConstraintViolation> violations, List<String> paths, List<EntityConstraintViolation> violationInPath, List<EntityConstraintViolation> violationOutPath){
        for (EntityConstraintViolation violation : violations) {
            if(!StringUtils.isEmpty(violation.getPath())){
                if(paths.contains(violation.getPath())){
                    violationInPath.add(violation);
                }else{
                    violationOutPath.add(violation);
                }
            }
        }
    }

    //PUBLIC METHODS - VALIDATE

    @Override
    public void validate(List<EntityConstraintViolation> violations) {
        if (!violations.isEmpty()) {
            throw new EntityValidationException(violations);
        }
    }

    @Override
    public void validateWithConfirm(List<EntityConstraintViolation> violations) {
        if (!violations.isEmpty()) {
            throw new EntityValidationWithConfirmException(violations);
        }
    }

    @Override
    public void validate(String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(message);
        throw new EntityValidationException(Collections.singletonList(violation));
    }

    @Override
    public void validateWithConfirm(String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(message);
        throw new EntityValidationWithConfirmException(Collections.singletonList(violation));
    }

    @Override
    public void validate(String path, String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(path, message);
        throw new EntityValidationException(Collections.singletonList(violation));
    }

    @Override
    public void validateWithConfirm(String path, String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(path, message);
        throw new EntityValidationWithConfirmException(Collections.singletonList(violation));
    }

    @Override
    public void validate(Object o, Class<?>... groups) {
        List<EntityConstraintViolation> violations = generateViolation(o, groups);
        validate(violations);
    }

    //PRIVATE METHODS

    /**
     * Pobiera wartość danego pola w danym objekcie.
     * @param field obiekt reprezentujacy pole
     * @param o obiekt
     * @return wartość pola
     */
    private Object getValue(Field field, Object o){
        field.setAccessible(true);
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Nie udało się pobrać wartosci z pola '%s'", field.getName()));
        }
    }
}

