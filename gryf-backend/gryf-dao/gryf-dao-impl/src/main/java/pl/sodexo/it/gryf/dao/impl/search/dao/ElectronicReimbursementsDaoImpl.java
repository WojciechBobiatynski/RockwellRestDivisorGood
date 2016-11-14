package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ElectronicReimbursementsSearchMapper;

import java.util.List;

/**
 * Implementacja dao do operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ElectronicReimbursementsDaoImpl implements ElectronicReimbursementsDao{

    @Autowired
    private ElectronicReimbursementsSearchMapper electronicReimbursementsSearchMapper;

    @Override
    public List<ElctRmbsDto> findEcltRmbsListByCriteria(ElctRmbsCriteria criteria) {
        return electronicReimbursementsSearchMapper.findEcltRmbsListByCriteria(criteria);
    }
}
