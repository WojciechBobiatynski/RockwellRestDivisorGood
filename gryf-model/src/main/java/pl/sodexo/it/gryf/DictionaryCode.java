package pl.sodexo.it.gryf;

import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.Sex;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseEntityType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationStatus;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwnerAidProduct;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachmentType;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementStatus;

/**
 * Created by tomasz.bilski.ext on 2015-07-02.
 */
public enum DictionaryCode {

    GR_APPLICATION_STATUSES(GrantApplicationStatus.class),
    ENT_ENTITY_TYPES(EnterpriseEntityType.class),
    ENT_SIZE_TYPES(EnterpriseSize.class),
    ORDER_FLOW_STATUSES(OrderFlowStatus.class),
    DELIVERY_STATUSES(ReimbursementDeliveryStatus.class),
    REIMBURSEMENT_STATUS(ReimbursementStatus.class),
    SEX(Sex.class),
    GRANT_OWNER_AID_PRODUCT(GrantOwnerAidProduct.class),
    ATTACHMENT_TYPES(ReimbursementAttachmentType.class);


    //FIELDS

    private Class<? extends DictionaryEntity> dictionaryClass;

    //CONSTRUCTORS

    DictionaryCode(Class<? extends DictionaryEntity> dictionaryClass){
        this.dictionaryClass = dictionaryClass;
    }

    //GETTERS

    public Class<? extends DictionaryEntity> getDictionaryClass() {
        return dictionaryClass;
    }
}
