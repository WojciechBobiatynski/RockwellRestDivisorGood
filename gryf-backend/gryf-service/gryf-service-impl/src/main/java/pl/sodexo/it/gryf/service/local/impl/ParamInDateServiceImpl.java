package pl.sodexo.it.gryf.service.local.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramLimitRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramParamRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantApplicationVersionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowForGrantProgramRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryParamRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;
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
@Transactional
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

    @Autowired
    private GrantProgramLimitRepository grantProgramLimitRepository;

    //PUBLIC METHODS

    @Override
    public GrantProgramProduct findGrantProgramProduct(Long grantProgramId, GrantProgramProduct.Type type, Date date, boolean mandatory){
        List<GrantProgramProduct> grantProgramProducts = grantProgramProductRepository.findByGrantProgramInDate(grantProgramId, date);
        if(grantProgramProducts.size() == 0){
            if(!mandatory){
                return null;
            }
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
    public TrainingCategoryParam findTrainingCategoryParam(ProductCalculateDto productCalculateDto, boolean mandatory){
        List<TrainingCategoryParam> params = trainingCategoryCatalogParamRepository.findByCategoryAndGrantProgramAndParticipantsInDate(
                productCalculateDto.getCategoryId(),  productCalculateDto.getGrantProgramId(), productCalculateDto.getMaxParticipants(), productCalculateDto.getDate());
        if(params.size() == 0){
            if(!mandatory){
                return null;
            }
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Nie znaleziono żadnego "
                            + "parametru dla kategorii [%s], programu dofinansowania [%s] obowiązującego dnia [%s]",
                    productCalculateDto.getCategoryId(),  productCalculateDto.getGrantProgramId(), productCalculateDto.getDate()));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.TI_TRAINING_CATEGORY_PARAMS. Dla danej "
                            + "dla kategorii [%s], programu dofinansowania [%s] znaleziono więcej niż jeden parametr obowiązujący dnia [%s]",
                    productCalculateDto.getCategoryId(),  productCalculateDto.getGrantProgramId(), productCalculateDto.getDate()));
        }
        return params.get(0);

    }

    @Override
    public OrderFlowForGrantProgram findOrderFlowForGrantProgram(Long grantProgramId, Date date, boolean mandatory) {
        List<OrderFlowForGrantProgram> params = orderFlowForGrantProgramRepository.findByGrantProgramInDate(grantProgramId, date);
        if (params.size() == 0) {
            if(!mandatory){
                return null;
            }
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla grant programu [%s]", grantProgramId));
        }
        if (params.size() > 1) {
            throw new RuntimeException(String.format("Dla danego grant programu [%s] znaleziono wiecej niż jeden order flow", grantProgramId));
        }
        return params.get(0);
    }

    @Override
    public OrderFlowForGrantApplicationVersion findOrderFlowForGrantApplicationVersion(Long versionId, Date date, boolean mandatory) {
        List<OrderFlowForGrantApplicationVersion> params = orderFlowForGrantApplicationVersionRepository.findByGrantApplicationVersionInDate(versionId, date);
        if (params.size() == 0) {
            if(!mandatory){
                return null;
            }
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla wersji [%s]", versionId));
        }
        if (params.size() > 1) {
            throw new RuntimeException(String.format("Dla danej wersji wniosku [%s] znaleziono wiecej niż jeden order flow", versionId));
        }
        return params.get(0);
    }

    @Override
    public GrantProgramParam findGrantProgramParam(Long grantProgramId, String paramTypeId, Date date, boolean mandatory){
        List<GrantProgramParam> params = grantProgramParamRepository.findByGrantProgramInDate(grantProgramId, paramTypeId, date);
        if(params.size() == 0){
            if(!mandatory){
                return null;
            }
            throw new RuntimeException(String.format("Dla danego programu dofinanoswania [%s] nie znalziono wartości dla parametru [%s] w danej dacie",
                    grantProgramId, paramTypeId));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Dla danego programu dofinanoswania [%s] znalziono wiecej niże jeden parametr [%s] w danej dacie",
                    grantProgramId, paramTypeId));
        }
        return params.get(0);
    }

    @Override
    public GrantProgramLimit findGrantProgramLimit(Long grantProgramId, String enterpriseSizeId, GrantProgramLimit.LimitType limitType, Date date, boolean mandatory){
        List<GrantProgramLimit> GrantProgramLimits = grantProgramLimitRepository.findByGrantProgramEntSizeLimitTypeInDate(grantProgramId, enterpriseSizeId, limitType, date);
        if(GrantProgramLimits.size() == 0){
            if(!mandatory){
                return null;
            }
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_LIMITS. Nie znaleziono żadnego limitu typu [%s] dla rozmiaru przedsiębiorstwa [%s] dla programu [%s] obowiązującego dnia [%s]",limitType, enterpriseSizeId, grantProgramId, date));
        }
        if(GrantProgramLimits.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_LIMITS. Dla danego programu [%s] znaleziono więcej niż jeden limit typu [%s] dla rozmiaru przedsiębiorstwa [%s] obowiązujący dnia [%s]", grantProgramId,limitType, enterpriseSizeId, date));
        }
        return GrantProgramLimits.get(0);
    }

}
