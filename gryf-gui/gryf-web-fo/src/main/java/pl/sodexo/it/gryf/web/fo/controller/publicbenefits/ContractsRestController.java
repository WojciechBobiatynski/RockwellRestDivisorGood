package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.web.fo.utils.UrlConstants;

import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
@RestController
@RequestMapping(value = UrlConstants.PUBLIC_BENEFITS_REST + "/contract", produces = "application/json;charset=UTF-8")
public class ContractsRestController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/grantProgramsDictionaries", method = RequestMethod.GET)
    @ResponseBody
    public List<GrantProgramDictionaryDTO> findGrantProgramsDictionaries() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS);
        return contractService.findGrantProgramsDictionaries();
    }

    @RequestMapping(value = "/contractTypes", method = RequestMethod.GET)
    @ResponseBody
    public List<DictionaryDTO> findContractTypes() {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS);
        return contractService.findContractTypesDictionaries();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Long saveContract(@RequestBody ContractDTO contractDTO) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS_MOD);
        return contractService.saveContract(contractDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ContractDTO getContract(@PathVariable Long id) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS);
        return contractService.findContract(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public Long updateContract(@PathVariable Long id, @RequestBody ContractDTO contractDTO) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS_MOD);
        GryfUtils.checkForUpdate(id, contractDTO.getId());

        contractService.updateContract(contractDTO);
        return contractDTO.getId();
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO dto) {
        securityChecker.assertServicePrivilege(Privileges.GRF_PBE_CONTRACTS);
        return contractService.findContracts(dto);
    }
}