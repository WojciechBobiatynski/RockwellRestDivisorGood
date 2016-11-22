package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryParam;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-11-18.
 */
public interface ParamInDateService {

    GrantProgramProduct findGrantProgramProduct(Long grantProgramId, GrantProgramProduct.Type type, Date date);

    TrainingCategoryParam findTrainingCategoryParam(String categoryId, Long grantProgramId, Date date);

    OrderFlowForGrantProgram findOrderFlowForGrantProgram(Long grantProgramId, Date date);

    OrderFlowForGrantApplicationVersion findOrderFlowForGrantApplicationVersion(Long versionId, Date date);

    GrantProgramParam findGrantProgramParam(Long grantProgramId, String paramTypeId, Date date);

}
