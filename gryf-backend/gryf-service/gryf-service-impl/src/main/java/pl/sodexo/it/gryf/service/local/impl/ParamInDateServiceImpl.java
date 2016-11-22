package pl.sodexo.it.gryf.service.local.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramParamRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantApplicationVersionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryParamRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-18.
 */
@Service
public class ParamInDateServiceImpl implements ParamInDateService {

    @Autowired
    private GrantProgramProductRepository grantProgramProductRepository;

    @Autowired
    private TrainingCategoryParamRepository trainingCategoryCatalogParamRepository;

    @Autowired
    private OrderFlowForGrantProgramRepository orderFlowForGrantProgramRepository;

    @Autowired
    private OrderFlowForGrantApplicationVersionRepository orderFlowForGrantApplicationVersionRepository;

    @Autowired
    private GrantProgramParamRepository grantProgramParamRepository;

    //PUBLIC METHODS

    @Override
    public GrantProgramProduct findGrantProgramProduct(Long grantProgramId, GrantProgramProduct.Type type, Date date){
        List<GrantProgramProduct> grantProgramProducts = grantProgramProductRepository.findByGrantProgramInDate(grantProgramId, date);
        if(grantProgramProducts.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Nie znaleziono żadnego "
                            + "produktu dla programu [%s] obowiązującego dnia [%s]",
                    grantProgramId, date));
        }
        if(grantProgramProducts.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej "
                            + "programu [%s] znaleziono więcej niż jeden produkt obowiązujący dnia [%s]",
                    grantProgramId, date));
        }

        GrantProgramProduct p = grantProgramProducts.get(0);
        if(type == GrantProgramProduct.Type.PBE_PRODUCT) {
            if (p.getPbeProduct() == null) {
                throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej programu " + "[%s] znaleziono jeden jeden produkt obowiązujący dnia [%s] ale bez ustawionej kolumny PBE_PRD_ID",
                        grantProgramId, date));
            }
        }
        if(type == GrantProgramProduct.Type.PRODUCT) {
            if (p.getProduct() == null) {
                throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej programu " + "[%s] znaleziono jeden jeden produkt obowiązujący dnia [%s] ale bez ustawionej kolumny PRD_ID",
                        grantProgramId, date));
            }
        }
        return p;
    }

    @Override
    public TrainingCategoryParam findTrainingCategoryParam(String categoryId, Long grantProgramId, Date date){
        List<TrainingCategoryParam> params = trainingCategoryCatalogParamRepository.findByCategoryAndGrantProgramInDate(
                categoryId,  grantProgramId, date);
        if(params.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Nie znaleziono żadnego "
                            + "parametru dla kategorii [%s], programu dofinansowania [%s] obowiązującego dnia [%s]",
                    categoryId, grantProgramId, date));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Dla danej "
                            + "dla kategorii [%s], programu dofinansowania [%s] znaleziono więcej niż jeden parametr obowiązujący dnia [%s]",
                    categoryId, grantProgramId, date));
        }
        return params.get(0);

    }

    @Override
    public OrderFlowForGrantProgram findOrderFlowForGrantProgram(Long grantProgramId, Date date) {
        List<OrderFlowForGrantProgram> params = orderFlowForGrantProgramRepository.findByGrantProgramInDate(grantProgramId, date);
        if (params.size() == 0) {
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla grant programu [%s]", grantProgramId));
        }
        if (params.size() > 1) {
            throw new RuntimeException(String.format("Dla danego grant programu [%s] znaleziono wiecej niż jeden order flow", grantProgramId));
        }
        return params.get(0);
    }

    @Override
    public OrderFlowForGrantApplicationVersion findOrderFlowForGrantApplicationVersion(Long versionId, Date date) {
        List<OrderFlowForGrantApplicationVersion> params = orderFlowForGrantApplicationVersionRepository.findByGrantApplicationVersionInDate(versionId, date);
        if (params.size() == 0) {
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla wersji [%s]", versionId));
        }
        if (params.size() > 1) {
            throw new RuntimeException(String.format("Dla danej wersji wniosku [%s] znaleziono wiecej niż jeden order flow", versionId));
        }
        return params.get(0);
    }

    @Override
    public GrantProgramParam findGrantProgramParam(Long grantProgramId, String paramTypeId, Date date){
        List<GrantProgramParam> params = grantProgramParamRepository.findByGrantProgramInDate(grantProgramId, paramTypeId, date);
        if(params.size() == 0){
            throw new RuntimeException(String.format("Dla danego programu dofinanoswania [%s] nie znalziono wartości dla parametru [%s] w danej dacie",
                    grantProgramId, paramTypeId));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Dla danego programu dofinanoswania [%s] znalziono wiecej niże jeden parametr [%s] w danej dacie",
                    grantProgramId, paramTypeId));
        }
        return params.get(0);
    }
}
