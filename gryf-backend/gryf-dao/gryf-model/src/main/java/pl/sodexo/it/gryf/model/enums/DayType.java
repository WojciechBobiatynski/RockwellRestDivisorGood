package pl.sodexo.it.gryf.model.enums;

public enum DayType{

    B("Roboczy"), C("Kalendarzowy");

    //FIELDS

    private String label;

    //CONSTRUCTORS

    private DayType(String label){
        this.label = label;
    }

    //GETTERS

    public String getLabel() {
        return label;
    }


}
