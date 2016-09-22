package pl.sodexo.it.gryf.dao.api.crud.repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GenericRepository<E, K extends Serializable> {

    E get(K key);

    E save(E entity);

    E update(E entity, K key);

    void delete(E entity);

    void refresh(E entity);

    List<E> findAll();

    void flush();
}
