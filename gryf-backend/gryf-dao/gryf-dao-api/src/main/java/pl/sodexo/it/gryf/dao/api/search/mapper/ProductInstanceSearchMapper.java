package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Mapper batisowy dla instacji produktów
 *
 * Created by jbentyn on 2016-10-17.
 */
public interface ProductInstanceSearchMapper {

    /**
     * Zwraca liczbe bonów, które sa gotowe do generowania numeru drukowanego
     *
     * @param productId - unikatowy numer produktu
     * @return liczba bonów gotowych do generacji numeru
     */
    Long countProductInstancesAvailableToNumberGeneration(@Param("productId") String productId);
}
