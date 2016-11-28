package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Klasa bazowa implementująca danu OrderFlowService.
 * Zawarta w niej jest podstawowa logika związana z OrderFlow.
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
public abstract class OrderFlowBaseService implements OrderFlowService {

    //FIELDS

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private GrantProgramProductRepository grantProgramProductRepository;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ParamInDateService paramInDateService;
            
    //PUBLIC METHODS

    @Override
    public Order createOrder(GrantApplication grantApplication, OrderFlow orderFlow){
        GrantApplicationBasicData basicData = grantApplication.getBasicData();
        validateCreateOrder(grantApplication);

        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(grantApplication.getProgram().getId(),
                                                            GrantProgramProduct.Type.PRODUCT, grantApplication.getReceiptDate(), true);

        Order order = new Order();
        order.setId(grantApplication.getId());
        order.setOrderFlow(orderFlow);
        order.setApplication(grantApplication);
        order.setGrantProgram(grantApplication.getProgram());
        order.setEnterprise(grantApplication.getEnterprise());
        order.setStatus(orderFlow.getInitialStatus());
        order.setOrderDate(grantApplication.getReceiptDate());
        order.setAddressCorr(basicData.getAddressCorr());
        order.setZipCodeCorrId((basicData.getZipCodeCorr() != null) ? basicData.getZipCodeCorr().getId() : null);
        order.setOperator(GryfUser.getLoggedUserLogin());
        order.setProduct(gpProduct.getProduct());
        
        return orderRepository.save(order);
    }

    @Override
    public Order createOrder(Contract contract, OrderFlow orderFlow, CreateOrderDTO dto) {
        Individual individual = contract.getIndividual();
        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(contract.getGrantProgram().getId(),
                                                                    GrantProgramProduct.Type.PBE_PRODUCT, new Date(), true);

        Order order = new Order();
        order.setGrantProgram(contract.getGrantProgram());
        order.setOrderFlow(orderFlow);
        order.setEnterprise(contract.getEnterprise());
        order.setStatus(orderFlow.getInitialStatus());
        order.setOrderDate(new Date());
        order.setAddressCorr(individual.getAddressCorr());
        order.setZipCodeCorrId((individual.getZipCodeCorr() != null) ? individual.getZipCodeCorr().getId() : null);
        order.setOperator(GryfUser.getLoggedUserLogin());
        order.setContract(contract);
        order.setPbeProduct(gpProduct.getPbeProduct());

        return orderRepository.save(order);
    }

    @Override
    public CreateOrderDTO createCreateOrderDTO(Contract contract){
        Individual individual = contract.getIndividual();
        Enterprise enterprise = contract.getEnterprise();
        GrantProgram grantProgram = contract.getGrantProgram();
        GrantProgramParam ocpParam = paramInDateService.findGrantProgramParam(grantProgram.getId(), GrantProgramParam.OWN_CONTRIBUTION_PERCENT, new Date(), true);
        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(contract.getGrantProgram().getId(),
                                                                GrantProgramProduct.Type.PBE_PRODUCT, new Date(), true);

        BigDecimal ownContributionPercent = new BigDecimal(ocpParam.getValue());

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setContractId(contract.getId());
        dto.setContractTypeName(contract.getContractType() != null ? contract.getContractType().getName() : null);
        dto.setContractSignDate(contract.getSignDate());
        dto.setContractExpiryDate(contract.getExpiryDate());

        dto.setGrantProgramName(grantProgram.getProgramName());
        if(individual != null){
            dto.setIndividualFirstName(individual.getFirstName());
            dto.setIndividualLastName(individual.getLastName());
            dto.setIndividualPesel(individual.getPesel());
        }
        if(enterprise != null){
            dto.setEnterpriseName(enterprise.getName());
            dto.setEnterpriseVatRegNum(enterprise.getVatRegNum());
        }
        if(enterprise != null){
            ZipCode zipCodeInvoice = enterprise.getZipCodeInvoice();
            if(enterprise.getAddressInvoice() != null && zipCodeInvoice != null){
                dto.setAddressInvoice(getAddressStr(enterprise.getAddressInvoice(), zipCodeInvoice));
            }
            ZipCode zipCodeCorr = enterprise.getZipCodeCorr();
            if(enterprise.getAddressCorr() != null && zipCodeCorr != null){
                dto.setAddressCorr(getAddressStr(enterprise.getAddressCorr(), zipCodeCorr));
            }
        }else if(individual != null){
            ZipCode zipCodeInvoice = individual.getZipCodeInvoice();
            if(individual.getAddressInvoice() != null && zipCodeInvoice != null){
                dto.setAddressInvoice(getAddressStr(individual.getAddressInvoice(), zipCodeInvoice));
            }
            ZipCode zipCodeCorr = individual.getZipCodeCorr();
            if(individual.getAddressCorr() != null && zipCodeCorr != null){
                dto.setAddressCorr(getAddressStr(individual.getAddressCorr(), zipCodeCorr));
            }
        }

        dto.setProductInstanceNum(null);
        dto.setProductInstanceAmount(gpProduct.getPbeProduct().getValue());
        dto.setOwnContributionPercen(ownContributionPercent);
        dto.setOwnContributionAmont(null);
        dto.setGrantAmount(null);
        dto.setOrderAmount(null);

        return dto;
    }
    
    /**
     * Metoda wykonuje sprawdzenia przed utworzeniem zamówienia
     * @param grantApplication wniosek o dofinansowanie
     */
    private void validateCreateOrder(GrantApplication grantApplication) {
        // wszystkie poprzednie zamówienia tego MŚP w tym programie muszą być już zakończone (w końcowych statusach)
        List <Order> orders = orderRepository.findByEnterpriseGrantProgramInNonFinalStatuses(grantApplication.getEnterprise().getId(), grantApplication.getProgram().getId());
        if (!orders.isEmpty()){
            gryfValidator.validate("Nie można przekazać wniosku do realizacji. Znaleziono " + orders.size() +
                                    " niezakończonych zamówień dla przedsiębiorstwa " + grantApplication.getEnterprise().getName() + " (id: " + grantApplication.getEnterprise().getId() + ") " + 
                    " w programie dofinasowania " + grantApplication.getProgram().getProgramName() +
                    ". Id niezakończonego zamówienia: " + orders.get(0).getId()) ;
       }
    }

    private String getAddressStr(String address, ZipCode zipcode){
        return String.format("%s, %s %s", address, zipcode.getZipCode(), zipcode.getCityName());
    }



}
