package pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementStatus;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.root.repository.GenericRepository;
import pl.sodexo.it.gryf.utils.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-07.
 */
@Repository
public class ReimbursementRepository extends GenericRepository<Reimbursement, Long> {

    public List<Reimbursement> findReimbursements(ReimbursementSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reimbursement> cq = cb.createQuery(Reimbursement.class);
        Root<Reimbursement> from = cq.from(Reimbursement.class);

        //PREDICATE
        List<Predicate> predicates = new ArrayList<>();
        if(dto.getId() != null){
            predicates.add(cb.equal(from.get(Reimbursement.ID_ATTR_NAME), dto.getId()));
        }
        if(!StringUtils.isEmpty(dto.getInvoiceNumber())){
            predicates.add(cb.like(cb.upper(from.<String>get(Reimbursement.INVOICE_NUMBER_ATTR_NAME)), getLikeWildCard(dto.getInvoiceNumber())));
        }
        if(!StringUtils.isEmpty(dto.getTrainingInstitutionVatRegNum())){
            predicates.add(cb.like(cb.upper(from.get(Reimbursement.REIMBURSEMENT_DELIVERY_ATTR_NAME).get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).
                                                        <String>get(TrainingInstitution.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionVatRegNum())));
        }
        if(!StringUtils.isEmpty(dto.getTrainingInstitutionName())){
            predicates.add(cb.like(cb.upper(from.get(Reimbursement.REIMBURSEMENT_DELIVERY_ATTR_NAME).get(ReimbursementDelivery.TRAINING_INSTITUTION_ATTR_NAME).
                    <String>get(TrainingInstitution.NAME_ATTR_NAME)), getLikeWildCard(dto.getTrainingInstitutionName())));
        }
        if(dto.getStatusId() != null){
            predicates.add(cb.equal(from.get(Reimbursement.STATUS_ATTR_NAME).get(ReimbursementStatus.STATUS_ID_ATTR_NAME), dto.getStatusId()));
        }
        if(dto.getReimbursementDeliveryId() != null){
            predicates.add(cb.equal(from.get(Reimbursement.REIMBURSEMENT_DELIVERY_ATTR_NAME).get(ReimbursementDelivery.ID_ATTR_NAME), dto.getReimbursementDeliveryId()));
        }
        if(dto.getDeliveryDateFrom() != null){
            addDateFrom(cb, predicates, from.get(Reimbursement.REIMBURSEMENT_DELIVERY_ATTR_NAME).<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateFrom());
        }
        if(dto.getDeliveryDateTo() != null){
            addDateTo(cb, predicates, from.get(Reimbursement.REIMBURSEMENT_DELIVERY_ATTR_NAME).<Date>get(ReimbursementDelivery.DELIVERY_DATE_ATTR_NAME), dto.getDeliveryDateTo());
        }
        if(dto.getAnnouncementDateFrom() != null){
            addDateFrom(cb, predicates, from.<Date>get(Reimbursement.ANNOUNCEMENT_DATE_ATTR_NAME), dto.getAnnouncementDateFrom());
        }
        if(dto.getAnnouncementDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(Reimbursement.ANNOUNCEMENT_DATE_ATTR_NAME), dto.getAnnouncementDateTo());
        }
        if(dto.getReimbursementDateFrom() != null){
            addDateFrom(cb, predicates, from.<Date>get(Reimbursement.REIMBURSEMENT_DATE_ATTR_NAME), dto.getReimbursementDateFrom());
        }
        if(dto.getReimbursementDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(Reimbursement.REIMBURSEMENT_DATE_ATTR_NAME), dto.getReimbursementDateTo());
        }
        Predicate[] predicatesTab = predicates.toArray(new Predicate[predicates.size()]);

        //SELECT
        cq.multiselect(from).where(predicatesTab).groupBy(from);

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<Reimbursement> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }
}
