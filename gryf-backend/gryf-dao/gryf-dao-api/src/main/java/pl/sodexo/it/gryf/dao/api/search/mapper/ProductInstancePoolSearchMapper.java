package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

import java.util.List;

/**
 * Mapper batisowy dla pól instancji bonów
 *
 * Created by akmiecinski on 2016-12-22.
 */
public interface ProductInstancePoolSearchMapper {

    /**
     * Znajduje wszystkie przeterminowane pule bonów dla kierunku kariera
     * @param criteria - kryteria użytkownika
     * @return lista pul bonów
     */
    List<PbeProductInstancePoolDto> findExpiredPoolInstances(@Param("criteria") UserCriteria criteria);

}
