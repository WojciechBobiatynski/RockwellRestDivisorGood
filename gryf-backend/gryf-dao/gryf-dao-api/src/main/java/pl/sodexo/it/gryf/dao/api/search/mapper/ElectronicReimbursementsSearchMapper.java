package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;

import java.util.List;

/**
 * Mapper do operacji na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ElectronicReimbursementsSearchMapper {

    /**
     * Metoda zwracająca listę rozliczeń na podstawie kryteriów wyszkuwiania
     *
     * @param criteria - kryteria wyszkuiwania
     * @return - lista rozliczeń
     */
    List<ElctRmbsDto> findEcltRmbsListByCriteria(@Param("criteria") ElctRmbsCriteria criteria);

    /**
     * Metoda zwracająca listę statusów rozliczeń
     *
     * @param criteria kryteria użytkownika
     * @return - lista statusów
     */
    List<SimpleDictionaryDto> findElctRmbsStatuses(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda znajdująca parametry do obliczenia składek na rolizczeniu dla instancji szkolenia
     *
     * @param criteria - kryteria użytkownika
     * @param trainingInstanceId - id instytucji szkoleniowej
     * @return
     */
    CalculationChargesParamsDto findCalculationChargesParamsForTrInstId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

    /**
     * Znajduje szczegóły rozliczenia dla bonów elektronicznych na podstawie Id
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsById(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Znajduje szczegóły rozliczenia podstawie Id instancji szkolenia
     * @param criteria - krytertia użytkownika
     * @param trainingInstanceId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsByTrainingInstanceId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

}
