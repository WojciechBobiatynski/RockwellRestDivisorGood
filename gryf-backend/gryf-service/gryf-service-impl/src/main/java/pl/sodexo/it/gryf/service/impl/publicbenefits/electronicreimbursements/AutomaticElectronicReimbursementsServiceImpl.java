package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.AutomaticElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsMailService;

import java.util.List;

/**
 * Serwis implementujący operacje na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Service
@Transactional
public class AutomaticElectronicReimbursementsServiceImpl implements AutomaticElectronicReimbursementsService {

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private ErmbsMailService ermbsMailService;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Long reimburse(Long ermbsId) {
        if(electronicReimbursementsService.isAutomaticErmbs(ermbsId)){
            electronicReimbursementsService.createDocuments(ermbsId);
            electronicReimbursementsService.printReports(ermbsId);
            electronicReimbursementsService.confirm(ermbsId);
            List<ErmbsMailDto> mailFromTemplates = ermbsMailService.createMailFromTemplates(ermbsId);
            mailFromTemplates.stream().forEach(ermbsMailDto -> ermbsMailService.sendErmbsMail(ermbsMailDto));
        } else {
            throw new IllegalStateException("Próbujesz rozliczyć szkolenie automatycznie, pomimo braku takiej możliwości");
        }
        return ermbsId;
    }
}
