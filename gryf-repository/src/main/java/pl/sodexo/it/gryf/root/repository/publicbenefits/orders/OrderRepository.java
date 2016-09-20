package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dto.SearchDto;
import pl.sodexo.it.gryf.dto.SortType;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;
import pl.sodexo.it.gryf.root.repository.GenericRepository;
import pl.sodexo.it.gryf.utils.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@Repository
public class OrderRepository extends GenericRepository<Order, Long> {

    public List<Object[]> findOrders(OrderSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Order> from = cq.from(Order.class);
        Join<Order, OrderElement> joinOrderElements = from.join(Order.ORDER_ELEMENTS_ATTR_NAME, JoinType.LEFT);
        Join<Order, GrantApplication> joinGrantApplications = from.join(Order.APPLICATION_ATTR_NAME, JoinType.LEFT);//ABY DZIALALO SORTOWANIE
        Join<Order, GrantApplication> joinEnterprises = from.join(Order.ENTERPRISE_ATTR_NAME, JoinType.LEFT);//ABY DZIALALO SORTOWANIE
        Join<Order, GrantApplication> joinIndividuals = from.join(Order.INDIVIDUAL_ATTR_NAME, JoinType.LEFT);//ABY DZIALALO SORTOWANIE

        //PREDICATE
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(joinOrderElements.get(OrderElement.COMPLETED_DATE_ATTR_NAME).isNull());
        if(dto.getId() != null){
            predicates.add(cb.equal(from.get(Order.ID_ATTR_NAME), dto.getId()));
        }
        if(dto.getStatusId() != null){
            predicates.add(cb.equal(from.get(Order.STATUS_ATTR_NAME).get(OrderFlowStatus.STATUS_ID_ATTR_NAME), dto.getStatusId()));
        }
        if(dto.getOrderDateFrom() != null){
            addDateFrom(cb, predicates, from.<Date>get(Order.ORDER_DATE_ATTR_NAME), dto.getOrderDateFrom());
        }
        if(dto.getOrderDateTo() != null){
            addDateTo(cb, predicates, from.<Date>get(Order.ORDER_DATE_ATTR_NAME), dto.getOrderDateTo());
        }
        if(dto.getApplicationId() != null){
            predicates.add(cb.equal(from.get(Order.APPLICATION_ATTR_NAME).get(GrantApplication.ID_ATTR_NAME), dto.getApplicationId()));
        }
        if(dto.getEnterpriseId() != null){
            predicates.add(cb.equal(from.get(Order.ENTERPRISE_ATTR_NAME).get(Enterprise.ID_ATTR_NAME), dto.getEnterpriseId()));
        }
        if(!StringUtils.isEmpty(dto.getEnterpriseName())){
            predicates.add(cb.like(cb.upper(from.get(Order.ENTERPRISE_ATTR_NAME).<String>get(Enterprise.NAME_ATTR_NAME)), getLikeWildCard(dto.getEnterpriseName())));
        }
        if(!StringUtils.isEmpty(dto.getVatRegNum())){
            predicates.add(cb.like(cb.upper(from.get(Order.ENTERPRISE_ATTR_NAME).<String>get(Enterprise.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getVatRegNum())));
        }
        if(dto.getIndividualId() != null){
            predicates.add(cb.equal(from.get(Order.INDIVIDUAL_ATTR_NAME).get(Individual.ID_ATTR_NAME), dto.getIndividualId()));
        }
        if(!StringUtils.isEmpty(dto.getIndividualFirstName())){
            predicates.add(cb.like(cb.upper(from.get(Order.INDIVIDUAL_ATTR_NAME).<String>get(Individual.FIRST_NAME_ATTR_NAME)), getLikeWildCard(dto.getIndividualFirstName())));
        }
        if(!StringUtils.isEmpty(dto.getIndividualLastName())){
            predicates.add(cb.like(cb.upper(from.get(Order.INDIVIDUAL_ATTR_NAME).<String>get(Individual.LAST_NAME_ATTR_NAME)), getLikeWildCard(dto.getIndividualLastName())));
        }
        if(!StringUtils.isEmpty(dto.getPesel())){
            predicates.add(cb.like(cb.upper(from.get(Order.INDIVIDUAL_ATTR_NAME).<String>get(Individual.PESEL_ATTR_NAME)), getLikeWildCard(dto.getPesel())));
        }
        if(!StringUtils.isEmpty(dto.getOperator())){
            predicates.add(cb.like(cb.upper(from.<String>get(Order.OPERATOR_ATTR_NAME)), getLikeWildCard(dto.getOperator())));
        }
        Predicate[] predicatesTab = predicates.toArray(new Predicate[predicates.size()]);

        //SELECT
        cq.multiselect(from, cb.least(joinOrderElements.<Date>get(OrderElement.REQUIRED_DATE_ATTR_NAME))).
                where(predicatesTab).groupBy(from, joinGrantApplications, joinEnterprises, joinIndividuals);

        //HAVING
        if(dto.getMinRequiredDateFrom() != null || dto.getMinRequiredDateTo() != null){
            List<Predicate> heavingPredicates = new ArrayList<>();
            if(dto.getMinRequiredDateFrom() != null){
                addDateFrom(cb, heavingPredicates, cb.least(joinOrderElements.<Date>get(OrderElement.REQUIRED_DATE_ATTR_NAME)), dto.getMinRequiredDateFrom());
            }
            if(dto.getMinRequiredDateTo() != null){
                addDateTo(cb, heavingPredicates, cb.least(joinOrderElements.<Date>get(OrderElement.REQUIRED_DATE_ATTR_NAME)), dto.getMinRequiredDateTo());
            }
            Predicate[] heavingPredicatesTab = heavingPredicates.toArray(new Predicate[heavingPredicates.size()]);
            cq.having(heavingPredicatesTab);
        }

        //SORT
        sortOrders(dto, cb, cq, from, joinOrderElements);

        //QUERY
        TypedQuery<Object[]> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }

    //PRIVATE METHODS

    /**
     * Metoda dodajaca sortowanie po elementach. Przy sortowaniu nie mozna bylo wykorzystac standartowej metody sort z klasy GenericRepository
     * poniewaz sortowanie po dacie wymaganej akcji odbywa się w sposób niestandardowy.
     * @param searchDTO obiekt wyszukujacy
     * @param cb criteria builder uzyty do budowy zapytania
     * @param cq criteri query użyte do budowy zapytania
     * @param fromOrder obiekt reprezentujacy clause from
     * @param joinOrderElements obiekt reprezentujacy klasule join
     * @return criteri query użyte do budowy zapytania
     */
    protected CriteriaQuery sortOrders(SearchDto searchDTO, CriteriaBuilder cb, CriteriaQuery cq, Root<Order> fromOrder, Join<Order, OrderElement> joinOrderElements) {
        if(searchDTO.getSortColumns() != null && searchDTO.getSortTypes() != null) {
            int length = searchDTO.getSortColumns().size();
            String[] sortColumnsTab = searchDTO.getSortColumns().toArray(new String[length]);
            SortType[] sortTypeTab = searchDTO.getSortTypes().toArray(new SortType[length]);
            javax.persistence.criteria.Order[] orderTab = new javax.persistence.criteria.Order[length];
            for (int i = 0; i < sortColumnsTab.length; i++) {

                //NIESTANDARDOWE SORTOWANIE
                if (OrderSearchQueryDTO.MIN_REQUIRED_DATE_ATTR_NAME.equals(sortColumnsTab[i])){
                    Expression<Date> path = cb.least(joinOrderElements.<Date>get(OrderElement.REQUIRED_DATE_ATTR_NAME));
                    orderTab[i] = getOrder(sortTypeTab[i], cb, path);

                //STANDARDOWE SORTOWANIE
                } else{
                    Path path = getPath(sortColumnsTab[i], fromOrder);
                    orderTab[i] = getOrder(sortTypeTab[i], cb, path);
                }
            }
            cq.orderBy(orderTab);
        }
        return cq;
    }
    
    
    public List<Order> findByEnterpriseGrantProgramInNonFinalStatuses(Long enterpriseId, Long grantProgramId){
        TypedQuery<Order> query = entityManager.createNamedQuery(Order.FIND_BY_ENT_PROGRAM_IN_NON_FINAL_ST, Order.class);
        query.setParameter("enterpriseId", enterpriseId);
        query.setParameter("grantProgramId", grantProgramId);
        return query.getResultList();
    }            
    
    public Long findGrantedVoucherNumberForEntAndProgram(Long enterpriseId, Long grantProgramId){
        TypedQuery<Long> query = entityManager.createNamedQuery(Order.FIND_GRANTED_VOUCH_NUM_FOR_ENT_AND_PROGRAM, Long.class);
        query.setParameter("enterpriseId", enterpriseId);
        query.setParameter("grantProgramId", grantProgramId);
        Long result = query.getSingleResult();
        return result != null ? result : 0;
    }        
            
}
