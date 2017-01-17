package pl.sodexo.it.gryf.model.asynch;

import pl.sodexo.it.gryf.model.api.DictionaryEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Isolution on 2016-12-01.
 */
public enum AsynchronizeJobType implements DictionaryEntity {

    IMPORT_CON(100l, "Import umów", "asynchJobImportService", "importContractService"),
    IMPORT_ORD(100l, "Import zamówień", "asynchJobImportService", "importOrderService"),
    IMPORT_TRA_INS(100l, "Import instytucji szkoleniowych", "asynchJobImportService", "importTrainingInstitutionService"),
    IMPORT_TRA_INS_V1(100l, "Import instytucji szkoleniowych - Stara wersja v1 (adresy rozdzielone)", "asynchJobImportService", "importTrainingInstitutionV1Service"),
    IMPORT_TRA(100l, "Import szkoleń", "asynchJobImportService", "importTrainingService"),
    IMPORT_OPI(100l, "Import ocen", "asynchJobImportService", "importOpinionDoneService"),
    ORDER_TRANS(null, "Zmiana statusu zamówienia", "asynchJobOrderTransitionService", null);

    private Long grantProgramId;
    private String label;
    private String serviceName;
    private String params;

    //CONSTRUCTORS

    AsynchronizeJobType(Long grantProgramId, String label, String serviceName, String params){
        this.grantProgramId = grantProgramId;
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

    public static Map<String, String> getAsMap(Optional<Long> grantProgramId, boolean onlyImportJobs) {
        Map<String, String> map = new HashMap<>();
        for(AsynchronizeJobType type : values()) {
            if(onlyImportJobs && type.getParams() == null
                    || grantProgramId.isPresent() && type.grantProgramId != grantProgramId.get()) {
                continue;
            }
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
