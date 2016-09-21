package pl.sodexo.it.gryf.common.exception.publicbenefits;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;

import java.util.List;

public class PeselIndividualExistException extends RuntimeException {

    //FIELDS

    private final List<IndividualSearchResultDTO> individuals;

    //CONSTRUCTORS

    public PeselIndividualExistException(String msg, List<IndividualSearchResultDTO> individuals) {
        super(msg);
        this.individuals = individuals;
    }

    //GETETRS

    public List<IndividualSearchResultDTO> getIndividuals() {
        return individuals;
    }
}
