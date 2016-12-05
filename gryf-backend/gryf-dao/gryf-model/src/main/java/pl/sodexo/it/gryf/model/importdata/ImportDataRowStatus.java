package pl.sodexo.it.gryf.model.importdata;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

/**
 * Created by Isolution on 2016-12-01.
 */
public enum ImportDataRowStatus implements DictionaryEntity {

    N("Nowy"), E("Błąd biznesowy"), F("Błąd krytyczny"),S("Poprawny");

    //FIELDS

    private String label;

    //CONSTRUCTORS

    private ImportDataRowStatus(String label){
        this.label = label;
    }

    //GETTERS

    public String getLabel() {
        return label;
    }

    //DICTIONARY METHODS

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
