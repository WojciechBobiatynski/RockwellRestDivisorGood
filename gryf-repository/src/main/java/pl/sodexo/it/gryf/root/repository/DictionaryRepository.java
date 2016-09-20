package pl.sodexo.it.gryf.root.repository;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.utils.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-02.
 */
@Repository
public class DictionaryRepository {

    //FIELDS

    @PersistenceContext
    private EntityManager entityManager;

    //PUBLIC METHODS

    /**
     * Zwraca liste encji słownikowych na podstawie klasy encji slownikowej.
     * @param clazz klasa encji słownikowej
     * @return lista objektów klasy encji słownikowej
     */
    public List<? extends DictionaryEntity> findDictionaries(Class<? extends DictionaryEntity> clazz) {

        //FROM
        DictionaryEntity dictionary = createEmptyDictionary(clazz);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root<? extends DictionaryEntity> from = cq.from(clazz);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(!StringUtils.isEmpty(dictionary.getActiveField())){
            predicatesList.add(cb.equal(from.get(dictionary.getActiveField()), true));
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        CriteriaQuery<? extends DictionaryEntity> select = cq.select(from).
                                            where(cb.and(predicatesTab)).
                                            orderBy(cb.asc(from.get(dictionary.getOrderField())));

        //QUERY
        TypedQuery<? extends DictionaryEntity> query = entityManager.createQuery(select);
        return query.getResultList();
    }

    /**
     * Zwraca listę słowników na podstawie natywnego sql który zwraca dwie wartosci.
     * @param nativeSql natywny sql
     * @return lista obiektow slownikowych
     */
    public List<DictionaryDTO> findDictionaries(String nativeSql){
        Query query = entityManager.createNativeQuery(nativeSql);
        List<Object[]> result = query.getResultList();
        return GryfUtils.constructList(result, new GryfUtils.ListConstructor<Object[], DictionaryDTO>() {
            public boolean isAddToList(Object[] input) {
                return true;
            }
            public DictionaryDTO create(Object[] input) {
                return new DictionaryDTO(input[0], (String) input[1]);
            }
        });
    }

    //PRIVATE METHODS

    public DictionaryEntity createEmptyDictionary(Class<? extends DictionaryEntity> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Nie odało się utworzyc pustego obiektu dla klasy: " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Nie odało się utworzyc pustego obiektu dla klasy: " + clazz);
        }
    }
}
