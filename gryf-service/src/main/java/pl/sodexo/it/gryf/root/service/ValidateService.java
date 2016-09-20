package pl.sodexo.it.gryf.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.FileContainerDTO;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.exception.EntityValidationException;
import pl.sodexo.it.gryf.exception.publicbenefits.grantapplications.EntityValidationWithConfirmException;
import pl.sodexo.it.gryf.utils.StringUtils;

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
public class ValidateService {

    //PRIVATE FIELDS

    @Autowired
    private ApplicationParametersService parameterService;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private Validator validator;

    //PUBLIC METHODS - GENERATE VIOLATION

    /**
     * Metoda generuje liste błędów na podstawie adnotacji javax.persistance w objekcie 'o'.
     * Metoda generuje liste błędów w opraciu o klasy groups.
     * @param o objekt walidowany
     * @param groups klasy na podstawie których jest walidacja
     * @return lista błędów
     */
    public  List<EntityConstraintViolation> generateViolation(Object o, Class<?>... groups) {
        List<EntityConstraintViolation> violations = new ArrayList<>();

        //JAVAX VALIDATION
        Set<ConstraintViolation<Object>> entityViolations = validator.validate(o, groups);
        for (ConstraintViolation<Object> v : entityViolations) {
            violations.add(new EntityConstraintViolation(StringUtils.toString(v.getPropertyPath()), v.getMessage(), v.getInvalidValue()));
        }

        return violations;
    }

    /**Dodaje błędy które wynikają z braku uprawnień do ustawienia (wykonania inserta) danego pola.
     * @param violations bazowa lista błędów
     * @param o objekt walidowany
     */
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

    /**
     * Dodaje błędy, które wynikają z dodanie zbyt wielkiego pliku.
     * @param violations bazowa lista błędów
     * @param prefix poczaek nazwy property do danej listy załaczników
     * @param filesContainers lista załączników
     */
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

    /**
     * Segreguje listę błędów (violations) ze wsględu na listę identyfikatorów tych błędów (paths). Gdy identyfikator błedu jest w
     * liscie ścieżek to wrzuca błąd do listy violationInPath, w przeciwnym razie wrzuca bład do listy violationOutPath.
     * @param violations lista błędów
     * @param paths lista identyfikatorów
     * @param violationInPath lista błędów które są w liscie identyfikatorów
     * @param violationOutPath lista błędów które nie są w liscie identyfikatorów
     */
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

    /**
     * Waliduje (generuje wyjątek z błędami) jeżeli lista błędów nie jest pusta.
     * @param violations lista błędów
     */
    public void validate(List<EntityConstraintViolation> violations) {
        if (!violations.isEmpty()) {
            throw new EntityValidationException(violations);
        }
    }

    /**
     * Waliduje (generuje wyjątek z błędami warunkowymi) jeżeli lista błędów nie jest pusta.
     * @param violations lista błędów
     */
    public void validateWithConfirm(List<EntityConstraintViolation> violations) {
        if (!violations.isEmpty()) {
            throw new EntityValidationWithConfirmException(violations);
        }
    }

    /**
     * Generuje wyjątek z błędem o treści podanej w parametrze
     * @param message komunikat błędu
     */
    public void validate(String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(message);
        throw new EntityValidationException(Collections.singletonList(violation));
    }

    /**
     * Generuje wyjątek warunkowy z błędem o treści podanej w parametrze
     * @param message komunikat błędu
     */
    public void validateWithConfirm(String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(message);
        throw new EntityValidationWithConfirmException(Collections.singletonList(violation));
    }

    /**
     * Generuje wyjątek z błędem o identyfikatorze i treści podanej w parametrzach
     * @param path identyfikator
     * @param message komunikat błędu
     */
    public void validate(String path, String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(path, message);
        throw new EntityValidationException(Collections.singletonList(violation));
    }

    /**
     * Generuje wyjątek warunkowy z błędem o identyfikatorze i treści podanej w parametrzach
     * @param path identyfikator
     * @param message komunikat błędu
     */
    public void validateWithConfirm(String path, String message) {
        EntityConstraintViolation violation = new EntityConstraintViolation(path, message);
        throw new EntityValidationWithConfirmException(Collections.singletonList(violation));
    }

    /**
     * Metoda waliduje na podstawie adnotacji javax.persistance w objekcie 'o'.
     * Metoda generuje liste błędów w opraciu o klasy groups a nastepnie generuje wyjątek z błędami.
     * @param o objekt walidowany
     * @param groups klasy na podstawie których jest walidacja
     */
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

