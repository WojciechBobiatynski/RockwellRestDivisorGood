package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.UnrsvPoolRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.UnreservedPoolReimbursementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;

import java.util.List;
import java.util.function.Consumer;

/**
 * Serwis do operacji na rozliczeniach niewykorzystanej puli bonów
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Service
@Transactional
public class UnreservedPoolReimbursementServiceImpl implements UnreservedPoolReimbursementService {

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private PbeProductInstancePoolLocalService pbeProductInstancePoolLocalService;

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createReimbursementForExpiredInstancesPool() {
        List<PbeProductInstancePoolDto> expiredPoolInstances = pbeProductInstancePoolLocalService.findExpiredPoolInstances();

        Consumer<PbeProductInstancePoolDto> pInsPoolCons = pbeProductInstancePool -> {
            Long ermbsId = electronicReimbursementsService.createEreimbursementForUnrsvPool(pbeProductInstancePool);
            pbeProductInstancePoolLocalService.expirePools(ermbsId);
            electronicReimbursementsService.createDocuments(ermbsId);
            electronicReimbursementsService.printReports(ermbsId);
        };
        expiredPoolInstances.stream().forEach(pInsPoolCons);
    }

    @Override
    public UnrsvPoolRmbsDto findUnrsvPoolRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findUnrsvPoolRmbsById(ermbsId);
    }
}
