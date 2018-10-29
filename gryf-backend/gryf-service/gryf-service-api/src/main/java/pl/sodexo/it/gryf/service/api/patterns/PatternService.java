package pl.sodexo.it.gryf.service.api.patterns;

/**
 * Interfejs usługi wzorcw np. wyszukiwania danych
 *
 */
public interface PatternService<ID extends  Long, CODE extends String, DEFAULT extends String> {

    String CONNECTOR = "_";

    String GRANT_PROGRAM_PATTERN_SERVICE = "GrantProgramPatternServiceImpl";
    String IMPORT_TRAINING_PATTERN_SERVICE =  "ImportTrainingPatternServiceImpl";

    /**
     *
     * Wybierz wzorzec na podstawie obiektu kontekstu
     *
     * @param patternContext
     * @return
     */
    String getPattern(PatternContext<ID, CODE, DEFAULT> patternContext);


    String getCode();

    String getPatternCodeName();
}
