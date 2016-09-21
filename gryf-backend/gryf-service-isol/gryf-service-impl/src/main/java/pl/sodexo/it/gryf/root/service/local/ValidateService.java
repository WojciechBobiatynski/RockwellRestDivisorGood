package pl.sodexo.it.gryf.root.service.local;

import pl.sodexo.it.gryf.common.dto.FileContainerDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ValidateService {

    /**
     * Metoda generuje liste błędów na podstawie adnotacji javax.persistance w objekcie 'o'.
     * Metoda generuje liste błędów w opraciu o klasy groups.
     * @param o objekt walidowany
     * @param groups klasy na podstawie których jest walidacja
     * @return lista błędów
     */
    List<EntityConstraintViolation> generateViolation(Object o, Class<?>... groups);

    /**Dodaje błędy które wynikają z braku uprawnień do ustawienia (wykonania inserta) danego pola.
     * @param violations bazowa lista błędów
     * @param o objekt walidowany
     */
    void addInsertablePrivilege(List<EntityConstraintViolation> violations, Object o);

    /**
     * Dodaje błędy, które wynikają z dodanie zbyt wielkiego pliku.
     * @param violations bazowa lista błędów
     * @param prefix poczaek nazwy property do danej listy załaczników
     * @param filesContainers lista załączników
     */
    void addFileMaxSizePrivilege(List<EntityConstraintViolation> violations, String prefix, List<? extends FileContainerDTO> filesContainers);

    /**
     * Segreguje listę błędów (violations) ze wsględu na listę identyfikatorów tych błędów (paths). Gdy identyfikator błedu jest w
     * liscie ścieżek to wrzuca błąd do listy violationInPath, w przeciwnym razie wrzuca bład do listy violationOutPath.
     * @param violations lista błędów
     * @param paths lista identyfikatorów
     * @param violationInPath lista błędów które są w liscie identyfikatorów
     * @param violationOutPath lista błędów które nie są w liscie identyfikatorów
     */
    void classifyByPath(List<EntityConstraintViolation> violations, List<String> paths, List<EntityConstraintViolation> violationInPath, List<EntityConstraintViolation> violationOutPath);

    /**
     * Waliduje (generuje wyjątek z błędami) jeżeli lista błędów nie jest pusta.
     * @param violations lista błędów
     */
    void validate(List<EntityConstraintViolation> violations);

    /**
     * Waliduje (generuje wyjątek z błędami warunkowymi) jeżeli lista błędów nie jest pusta.
     * @param violations lista błędów
     */
    void validateWithConfirm(List<EntityConstraintViolation> violations);

    /**
     * Generuje wyjątek z błędem o treści podanej w parametrze
     * @param message komunikat błędu
     */
    void validate(String message);

    /**
     * Generuje wyjątek warunkowy z błędem o treści podanej w parametrze
     * @param message komunikat błędu
     */
    void validateWithConfirm(String message);

    /**
     * Generuje wyjątek z błędem o identyfikatorze i treści podanej w parametrzach
     * @param path identyfikator
     * @param message komunikat błędu
     */
    void validate(String path, String message);

    /**
     * Generuje wyjątek warunkowy z błędem o identyfikatorze i treści podanej w parametrzach
     * @param path identyfikator
     * @param message komunikat błędu
     */
    void validateWithConfirm(String path, String message);

    /**
     * Metoda waliduje na podstawie adnotacji javax.persistance w objekcie 'o'.
     * Metoda generuje liste błędów w opraciu o klasy groups a nastepnie generuje wyjątek z błędami.
     * @param o objekt walidowany
     * @param groups klasy na podstawie których jest walidacja
     */
    void validate(Object o, Class<?>... groups);
}
