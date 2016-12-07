package pl.sodexo.it.gryf.model.asynch;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isolution on 2016-12-01.
 */
public enum AsynchronizeJobType implements DictionaryEntity {

    IMPORT_CON("Import umów", "asynchJobImportService", "importContractService"),
    IMPORT_ORD("Błąd biznesowy", "asynchJobImportService", "importOrderService"),
    IMPORT_TRA_INS("Import instytucji szkoleniowych", "asynchJobImportService", "importTrainingInstitutionService"),
    IMPORT_TRA("Import szkoleń", "asynchJobImportService", "importTrainingService"),
    ORDER_TRANS("Zmiana statusu zamówienia", "asynchJobOrderTransitionService", null);

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

    public static Map<String, String> getAsMap() {
        Map<String, String> map = new HashMap<>();
        for(AsynchronizeJobType type : values()) {
            map.put(type.name(), type.label);
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
