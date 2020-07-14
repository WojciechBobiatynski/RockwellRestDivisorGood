package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesOrderParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsLineDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementLineRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.EreimbursementLine;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementLinesService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.EreimbursementLineDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis implementujący operacje na liniach częściowych e-rozliczeń
 * <p>
 * Created by dptasyznski on 14.07.2020.
 */
@Service
@Transactional
public class ElectronicReimbursementLinesServiceImpl implements ElectronicReimbursementLinesService {

    @Autowired
    private EreimbursementLineDtoMapper ereimbursementLineDtoMapper;

    @Autowired
    private EreimbursementLineRepository ereimbursementLineRepository;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Override
    public Long saveEreimbursementLine(ElctRmbsLineDto elctRmbsLineDto) {
        EreimbursementLine ereimbursementLine = ereimbursementLineDtoMapper.convert(elctRmbsLineDto);
        ereimbursementLine = ereimbursementLine.getId() != null ? ereimbursementLineRepository.update(ereimbursementLine, ereimbursementLine.getId()) : ereimbursementLineRepository.save(ereimbursementLine);
        ereimbursementLineRepository.flush();
        return ereimbursementLine.getId();
    }

    @Override
    public List<Long> getElctRmbsLineIds(ElctRmbsHeadDto elctRmbsHeadDto, List<CalculationChargesOrderParamsDto> orderParams) {
        if (elctRmbsHeadDto.getErmbsId() != null) {
            Ereimbursement ereimbursement = ereimbursementRepository.get(elctRmbsHeadDto.getErmbsId());
            /*
            List<EreimbursementLine> ereimbursementLines = ereimbursementLineRepository.getListByEreimbursement(ereimbursement);
            if (ereimbursementLines.size() == orderParams.size()) {
                return ereimbursementLines.stream().map(EreimbursementLine::getId).collect(Collectors.toList());
            }*/
            ereimbursementLineRepository.deleteListByEreimbursement(ereimbursement);
            ereimbursementLineRepository.flush();
        }
        return new ArrayList<>();
    }
}
