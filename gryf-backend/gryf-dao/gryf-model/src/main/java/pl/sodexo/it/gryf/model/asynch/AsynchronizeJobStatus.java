package pl.sodexo.it.gryf.model.asynch;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isolution on 2016-12-01.
 */
public enum AsynchronizeJobStatus implements DictionaryEntity {

    N("Nowy"), P("Przetwarzany"), E("Błąd biznesowy"), F("Błąd krytyczny"), S("Poprawny"), C("Częściowo poprawny");


    private String label;

    //CONSTRUCTORS

    private AsynchronizeJobStatus(String label){
        this.label = label;
    }

    //GETTERS

    public String getLabel() {
        return label;
    }

    //DICTIONARY METHODS

    public static Map<String, String> getAsMap() {
        Map<String, String> map = new HashMap<>();
        for(AsynchronizeJobStatus status : values()) {
            map.put(status.name(), status.label);
        }
        return map;
    }

    @Override
    public Object getDictionaryId() {
        return name();
    }

    @Override
    public String getDictionaryName() {
        return label;
    }

    @Override
    public String getOrderField(){
        return null;
    }

    @Override
    public String getActiveField() {
        return null;
    }

}
