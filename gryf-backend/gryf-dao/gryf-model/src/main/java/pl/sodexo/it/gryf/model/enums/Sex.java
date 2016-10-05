package pl.sodexo.it.gryf.model.enums;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
public enum Sex implements DictionaryEntity {

    K("Kobieta"), M("Mężczyzna");

    //FIELDS

    private String label;

    //CONSTRUCTORS

    private Sex(String label){
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
