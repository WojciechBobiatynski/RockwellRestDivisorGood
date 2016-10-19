package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementDeliveryRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDeliveryStatus;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Repository
public class ReimbursementDeliveryRepositoryImpl extends GenericRepositoryImpl<ReimbursementDelivery, Long> implements ReimbursementDeliveryRepository {

    @Override
    public Long findRegisterReimbursementDeliveriesCount(Long trainingInstitutionId, Date dateFrom, Date dateTo){
        String nativeSql = "select count(*) " +
                "from APP_PBE.REIMBURSEMENT_DELIVERIES " +
                "where TRAINING_INSTITUTION_ID = ?1 " +
                "and  MASTER_DELIVERY_ID IS NULL " +
                "and ((STATUS_ID in ('" + ReimbursementDeliveryStatus.ORDERED_CODE + "') AND TRUNC(PK_GRF_UTILS.Next_Business_Day(PLANNED_RECEIPT_DATE)) BETWEEN ?2 AND ?3) " +
                "     or (STATUS_ID in ('" + ReimbursementDeliveryStatus.DELIVERED_CODE + "', '" + ReimbursementDeliveryStatus.SCANNED_CODE + "')  and TRUNC(DELIVERY_DATE) BETWEEN ?2 AND ?3)) ";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, trainingInstitutionId);
        query.setParameter(2, dateFrom);
        query.setParameter(3, dateTo);
        BigDecimal count = (BigDecimal)query.getSingleResult();
        return (count != null) ? count.longValue() : 0;
    }

    @Override
    public Long findAnnouncedDeliveryCountInDate(Long trainingInstitutionId, Date dateFrom, Date dateTo){
        TypedQuery<Long> query = entityManager.createNamedQuery(ReimbursementDelivery.FIND_ANNOUNCED_DELIVERY_COUNT_IN_DATE, Long.class);
        query.setParameter("trainingInstitutionId", trainingInstitutionId);
        query.setParameter("statuses", Arrays.asList(ReimbursementDeliveryStatus.DELIVERED_CODE, ReimbursementDeliveryStatus.SCANNED_CODE));
        query.setParameter("dateFrom", dateFrom);
        query.setParameter("dateTo", dateTo);
        return query.getSingleResult();

    }

    @Override
    public List<ReimbursementDelivery> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReimbursementDelivery> cq = cb.createQuery(ReimbursementDelivery.class);
        Root<ReimbursementDelivery> from = cq.from(ReimbursementDelivery.class);

        //PREDICATE
        List<Predicate> predicates = new ArrayList<>();
        if(dto.getId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.ID_ATTR_NAME), dto.getId()));
        }
        if(dto.getStatusId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.STATUS_ATTR_NAME).get(ReimbursementDeliveryStatus.STATUS_ID_ATTR_NAME), dto.getStatusId()));
        }
        if(dto.getTrainingInstitutionId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).get(TrainingInstitution.ID_ATTR_NAME), dto.getTrainingInstitutionId()));
        }
        if(!GryfStringUtils.isEmpty(dto.getTrainingInstitutionName())){
            predicates.add(cb.like(cb.upper(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).<String>get(TrainingInstitution.NAME_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionName())));
        }
        if(!GryfStringUtils.isEmpty(dto.getTrainingInstitutionVatRegNum())){
            predicates.add(cb.like(cb.upper(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).<String>get(TrainingInstitution.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionVatRegNum())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryAddress())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_ADDRESS_ATTR_NAME)), getLikeWildCard(dto.getDeliveryAddress())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryZipCode())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getDeliveryZipCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryCityName())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getDeliveryCityName())));
        }
        if(dto.getPlannedReceiptDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.PLANNED_RECEIPT_DATE_ATTR_NAME), dto.getPlannedReceiptDateFrom());
        }
        if(dto.getPlannedReceiptDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.PLANNED_RECEIPT_DATE_ATTR_NAME), dto.getPlannedReceiptDateTo());
        }
        if(dto.getRequestDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.REQUEST_DATE_ATTR_NAME), dto.getRequestDateFrom());
        }
        if(dto.getRequestDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.REQUEST_DATE_ATTR_NAME), dto.getRequestDateTo());
        }
        if(dto.getDeliveryDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateFrom());
        }
        if(dto.getDeliveryDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateTo());
        }
        Predicate[] predicatesTab = predicates.toArray(new Predicate[predicates.size()]);

        //SELECT
        cq.multiselect(from).where(predicatesTab).groupBy(from);

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<ReimbursementDelivery> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }
    
    @Override
    public List<ReimbursementDelivery> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReimbursementDelivery> cq = cb.createQuery(ReimbursementDelivery.class);
        Root<ReimbursementDelivery> from = cq.from(ReimbursementDelivery.class);

        //PREDICATE
        List<Predicate> predicates = new ArrayList<>();
        if(dto.getId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.ID_ATTR_NAME), dto.getId()));
        }
        if(dto.getStatusId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.STATUS_ATTR_NAME).get(ReimbursementDeliveryStatus.STATUS_ID_ATTR_NAME), dto.getStatusId()));
        }
        if(dto.getTrainingInstitutionId() != null){
            predicates.add(cb.equal(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).get(TrainingInstitution.ID_ATTR_NAME), dto.getTrainingInstitutionId()));
        }
        if(!GryfStringUtils.isEmpty(dto.getTrainingInstitutionName())){
            predicates.add(cb.like(cb.upper(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).<String>get(TrainingInstitution.NAME_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionName())));
        }
        if(!GryfStringUtils.isEmpty(dto.getTrainingInstitutionVatRegNum())){
            predicates.add(cb.like(cb.upper(from.get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).<String>get(TrainingInstitution.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionVatRegNum())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryAddress())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_ADDRESS_ATTR_NAME)), getLikeWildCard(dto.getDeliveryAddress())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryZipCode())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getDeliveryZipCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getDeliveryCityName())){
            predicates.add(cb.like(cb.upper(from.<String>get(ReimbursementDelivery.DELIVERY_CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getDeliveryCityName())));
        }
        if(dto.getPlannedReceiptDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.PLANNED_RECEIPT_DATE_ATTR_NAME), dto.getPlannedReceiptDateFrom());
        }
        if(dto.getPlannedReceiptDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.PLANNED_RECEIPT_DATE_ATTR_NAME), dto.getPlannedReceiptDateTo());
        }
        if(dto.getRequestDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.REQUEST_DATE_ATTR_NAME), dto.getRequestDateFrom());
        }
        if(dto.getRequestDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.REQUEST_DATE_ATTR_NAME), dto.getRequestDateTo());
        }
        if(dto.getDeliveryDateFrom() != null){
            addDateFrom(cb,  predicates, from.<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateFrom());
        }
        if(dto.getDeliveryDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateTo());
        }

        predicates.add(cb.or(cb.equal(from.get(ReimbursementDelivery.STATUS_ATTR_NAME).get(ReimbursementDeliveryStatus.STATUS_ID_ATTR_NAME), ReimbursementDeliveryStatus.SCANNED_CODE ),
                             cb.equal(from.get(ReimbursementDelivery.STATUS_ATTR_NAME).get(ReimbursementDeliveryStatus.STATUS_ID_ATTR_NAME), ReimbursementDeliveryStatus.DELIVERED_CODE )));
        
        
        Predicate[] predicatesTab = predicates.toArray(new Predicate[predicates.size()]);

        //SELECT
        cq.multiselect(from).where(predicatesTab).groupBy(from);

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<ReimbursementDelivery> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }
    
}
