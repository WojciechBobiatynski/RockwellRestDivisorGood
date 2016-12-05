package pl.sodexo.it.gryf.model.asynch;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

/**
 * Created by Isolution on 2016-12-01.
 */
public enum AsynchronizeJobType implements DictionaryEntity {

    IMPORT_CON("Import umów", "asynchJobImportServce", "importContractDataService"),
    IMPORT_ORD("Błąd biznesowy", "asynchJobImportServce", "importOrderDataService"),
    IMPORT_TRA_INS("Import instytucji szkoleniowych", "asynchJobImportServce", "importTrainingInstitutionDataService"),
    IMPORT_TRA("Import szkoleń", "asynchJobImportServce", "importTrainingDataService");


    private String label;

    private String serviceName;

    private String params;

    //CONSTRUCTORS

    AsynchronizeJobType(String label, String serviceName, String params){
        this.label = label;
        this.serviceName = serviceName;
        this.params = params;
    }

    //GETTERS

    public String getLabel() {
        return label;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getParams() {
        return params;
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
