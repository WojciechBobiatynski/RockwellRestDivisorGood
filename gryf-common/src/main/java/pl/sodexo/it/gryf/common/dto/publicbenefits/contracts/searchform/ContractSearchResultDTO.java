package pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;

import java.util.Date;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString
public class ContractSearchResultDTO {

    private Long contractId;
    private Date signDate;
    private Date expiryDate;

    private DictionaryDTO contractType;
    private GrantProgramDictionaryDTO grantProgram;
    private IndividualSearchResultDTO individual;
    private EnterpriseSearchResultDTO enterprise;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public DictionaryDTO getContractType() {
        return contractType;
    }

    public void setContractType(DictionaryDTO contractType) {
        this.contractType = contractType;
    }

    public GrantProgramDictionaryDTO getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgramDictionaryDTO grantProgram) {
        this.grantProgram = grantProgram;
    }

    public IndividualSearchResultDTO getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualSearchResultDTO individual) {
        this.individual = individual;
    }

    public EnterpriseSearchResultDTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseSearchResultDTO enterprise) {
        this.enterprise = enterprise;
    }
}
