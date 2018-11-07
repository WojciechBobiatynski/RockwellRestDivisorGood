package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.ContractPbeProductInstancePoolDto;
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
     * @return lista puli bonów
     */
    List<PbeProductInstancePoolDto> findExpiredPoolInstances(@Param("criteria") UserCriteria criteria);

    /**
     * Znajduje pule bonów dla danej umowy
     * @param criteria - kryteria użytkownika
     * @param contractId - id umowy
     * @return lista puli bonów
     */
    List<ContractPbeProductInstancePoolDto> findPoolInstancesByContractId(@Param("criteria") UserCriteria criteria, @Param("contractId") String contractId);

}
