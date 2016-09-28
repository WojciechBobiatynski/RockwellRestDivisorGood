package pl.sodexo.it.gryf.service.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa bazowa wszystkich maperów. Zawiera szkielety metod konwertujących typ żródłowy na typ docelowy.
 *
 * Created by jbentyn on 2016-09-26.
 */
public abstract class GenericMapper<Source, Destination> {

    protected abstract Destination initDestination();

    protected abstract void map(Source source, Destination destination);

    public Destination convert(Source source) {
        Destination destination = initDestination();
        if (source != null) {
            map(source, destination);
        }
        return destination;
    }

    public List<Destination> convert(List<Source> sourceList) {
        List<Destination> result = new ArrayList<>();
        if (sourceList != null) {
            for(Source source:sourceList){
                result.add(convert(source));
            }
        }
        return result;
    }
}
