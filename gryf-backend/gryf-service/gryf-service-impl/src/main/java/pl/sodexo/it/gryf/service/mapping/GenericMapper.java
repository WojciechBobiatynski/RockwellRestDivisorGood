package pl.sodexo.it.gryf.service.mapping;

import java.util.ArrayList;
import java.util.Collections;
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
        if (source == null)
            return null;

        Destination destination = initDestination();
        map(source, destination);
        return destination;
    }

    public List<Destination> convert(List<Source> sourceList) {
        if (sourceList == null)
            return Collections.emptyList();

        List<Destination> result = new ArrayList<>();
        for (Source source : sourceList) {
            result.add(convert(source));
        }
        return result;
    }
}
