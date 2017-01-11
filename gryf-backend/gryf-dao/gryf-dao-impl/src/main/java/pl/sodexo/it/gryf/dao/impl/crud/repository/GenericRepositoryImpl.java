package pl.sodexo.it.gryf.dao.impl.crud.repository;

import pl.sodexo.it.gryf.common.dto.api.SearchDto;
import pl.sodexo.it.gryf.common.enums.SortType;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomasz.Bilski on 2015-06-08.
 */
public class GenericRepositoryImpl<E, K extends Serializable> implements GenericRepository<E, K> {

    //PROTECTED FIELDS

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<E> entityClass;

    //CONSTRUCTORS

    public GenericRepositoryImpl(){
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    //PUBLIC METHODS

    @Override
    public E get(K key){
        return entityManager.find(entityClass, key);
    }

    @Override
    public E save(E entity){
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public E update(E entity, K key){
        try {
            return entityManager.merge(entity);
        } catch (OptimisticLockException ole) {
            if(entity instanceof VersionableEntity) {
                VersionableEntity newEntity = (VersionableEntity)entityManager.find(entityClass, key);
                throw new StaleDataException(key, newEntity);
            }
            else{
                throw ole;
            }
        }
    }

    @Override
    public void delete(E entity){
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
    }

    @Override
    public void refresh(E entity){
        entityManager.refresh(entity);
    }

    @Override
    public List<E> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<E> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public void flush(){
        entityManager.flush();
    }

    //SORT METHODS

    protected CriteriaQuery sort(SearchDto searchDTO, CriteriaBuilder cb, CriteriaQuery cg, Root from) {
        if(searchDTO.getSortColumns() != null && searchDTO.getSortTypes() != null) {
            int length = searchDTO.getSortColumns().size();
            String[] sortColumnsTab = searchDTO.getSortColumns().toArray(new String[length]);
            SortType[] sortTypeTab = searchDTO.getSortTypes().toArray(new SortType[length]);
            Order[] orderTab = new Order[length];
            for (int i = 0; i < sortColumnsTab.length; i++) {
                Path path = getPath(sortColumnsTab[i], from);
                orderTab[i] = getOrder(sortTypeTab[i], cb, path);
            }
            cg.orderBy(orderTab);
        }
        return cg;
    }

    protected void limit(SearchDto searchDTO, TypedQuery query){
        if(searchDTO.getLimit() != null){
            query.setMaxResults(searchDTO.getLimit() + 1);
        }
    }

    protected String getLikeWildCard(String val){
        return (val != null) ? val.toUpperCase() : null;
    }

    protected Order getOrder(SortType sortType, CriteriaBuilder cb, Expression expression){
        if(sortType != null){
            switch (sortType){
                case ASC:
                    return cb.asc(expression);
                case DESC:
                    return cb.desc(expression);
            }
        }
        return cb.asc(expression);
    }

    protected Path<E> getPath(String dotPath, Root<E> from){
        String[] tablePath = dotPath.split("\\.");
        Path<E> path = from.get(tablePath[0]);
        for (int i = 1; i < tablePath.length; i++) {
            path = path.get(tablePath[i].trim());
        }
        return path;
    }

    protected String getPathStr(String ... fields){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]);
            if(i < fields.length - 1){
                sb.append(".");
            }
        }
        return sb.toString();
    }

    protected void addDateFrom(CriteriaBuilder cb,  List<Predicate> predicatesList, Expression<Date> path, Date dateFrom){
        Date dateStartDay = GryfUtils.getStartDay(dateFrom);
        predicatesList.add(cb.greaterThanOrEqualTo(path, dateStartDay));
    }

    protected void addDateTo(CriteriaBuilder cb,  List<Predicate> predicatesList, Expression<Date> path, Date dateTo){
        Date dateEndDay = GryfUtils.getEndDay(dateTo);
        predicatesList.add(cb.lessThanOrEqualTo(path, dateEndDay));
    }

    protected void addDateEqual(CriteriaBuilder cb,  List<Predicate> predicatesList, Expression<Date> path, Date date){
        predicatesList.add(cb.equal(path, date));
    }
}
