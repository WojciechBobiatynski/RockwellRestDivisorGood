package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseSize;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries.ZipCodeDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.ZipCodeEntityMapper;

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
    private GryfValidator gryfValidator;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    //PUBLIC METHODS

    @Override
    public Order createOrder(GrantApplication grantApplication, OrderFlow orderFlow){
        validateCreateOrder(grantApplication);

        GrantApplicationBasicData basicData = grantApplication.getBasicData();
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
        order.setZipCodeCorr(basicData.getZipCodeCorr());
        order.setOperator(GryfUser.getLoggedUserLogin());
        order.setProduct(gpProduct.getProduct());
        
        return orderRepository.save(order);
    }

    @Override
    public Order createOrder(Contract contract, OrderFlow orderFlow, CreateOrderDTO dto) {
        validateCreateOrder(dto, contract);

        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(contract.getGrantProgram().getId(),
                                                                    GrantProgramProduct.Type.PBE_PRODUCT, new Date(), true);
        Order order = new Order();
        order.setExternalOrderId(dto.getExternalOrderId());
        order.setGrantProgram(contract.getGrantProgram());
        order.setOrderFlow(orderFlow);
        order.setEnterprise(contract.getEnterprise());
        order.setStatus(orderFlow.getInitialStatus());
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : new Date());
        order.setVouchersNumber(dto.getProductInstanceNum());
        order.setAddressCorr(dto.getAddressCorr());
        order.setZipCodeCorr(zipCodeDtoMapper.convert(dto.getZipCodeCorr()));
        order.setOperator(GryfUser.getLoggedUserLogin());
        order.setContract(contract);
        order.setPbeProduct(gpProduct.getPbeProduct());
        order.setOwnContributionPercentage(contract.getOwnContributionPercentage());

        return order;
    }

    @Override
    public CreateOrderDTO createCreateOrderDTO(Contract contract){
        Individual individual = contract.getIndividual();
        Enterprise enterprise = contract.getEnterprise();
        GrantProgram grantProgram = contract.getGrantProgram();
        GrantProgramProduct gpProduct = paramInDateService.findGrantProgramProduct(contract.getGrantProgram().getId(),
                                                                GrantProgramProduct.Type.PBE_PRODUCT, new Date(), true);

        BigDecimal ownContributionPercent = contract.getOwnContributionPercentage();

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

        //ADRESY
        if(enterprise != null){
            setAddresses(dto, enterprise.getAddressInvoice(), enterprise.getZipCodeInvoice(),
                                enterprise.getAddressCorr(), enterprise.getZipCodeCorr());
        }else if(individual != null){
            setAddresses(dto, individual.getAddressInvoice(), individual.getZipCodeInvoice(),
                                individual.getAddressCorr(), individual.getZipCodeCorr());
        }

        dto.setExternalOrderId(null);
        dto.setOrderDate(new Date());
        dto.setProductInstanceNum(null);
        dto.setProductInstanceAmount(gpProduct.getPbeProduct().getValue());
        dto.setOwnContributionPercent(ownContributionPercent);
        dto.setOwnContributionAmont(null);
        dto.setGrantAmount(null);
        dto.setOrderAmount(null);

        return dto;
    }

    //PRIVATE METHODS

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

    /**
     * Metoda wykonuje sprawdzenia przed utworzeniem zamówienia
     * @param dto obiekt z danymi do utworzenia
     * @param contract umowa do której dodajemy zamowienie
     */
    private void validateCreateOrder(CreateOrderDTO dto, Contract contract){
        List<EntityConstraintViolation> violation = Lists.newArrayList();
        validateMaxOrderForContract(violation, contract);
        validateMaxProductInstanceForContract(violation, contract, dto);
        gryfValidator.validate(violation);
    }

    private void validateMaxOrderForContract(List<EntityConstraintViolation> violation, Contract contract){

        GrantProgram grantProgram = contract.getGrantProgram();
        GrantProgramLimit limit =  paramInDateService.findGrantProgramLimit(grantProgram.getId(), EnterpriseSize.SELF_CODE,
                                                                            GrantProgramLimit.LimitType.ORDNUMLIM,
                                                                            new Date(), false);

        if(limit != null && limit.getLimitValue() != null){
            int maxOrders = limit.getLimitValue().intValue();
            int notCanceledOrderNum = orderRepository.countNotCanceledOrdersByContract(contract.getId());
            notCanceledOrderNum ++;
            if(maxOrders < notCanceledOrderNum){
                violation.add(new EntityConstraintViolation(String.format("Przekroczono maksymalną ilość zamowień (%s) dla jednej umowy - "
                        + "ilość po dodaniu danego zamówienia: %s", maxOrders, notCanceledOrderNum)));
            }
        }
    }
    private void validateMaxProductInstanceForContract(List<EntityConstraintViolation> violation, Contract contract, CreateOrderDTO dto){

        GrantProgram grantProgram = contract.getGrantProgram();
        GrantProgramLimit limit =  paramInDateService.findGrantProgramLimit(grantProgram.getId(), EnterpriseSize.SELF_CODE,
                                                                            GrantProgramLimit.LimitType.ORDVOULIM,
                                                                            new Date(), false);
        if(limit != null && limit.getLimitValue() != null){
            int maxProductNum = limit.getLimitValue().intValue();
            int notCanceledOrderNum = orderRepository.sumProductInstanceNumInNotCanceledOrdersByContract(contract.getId());
            notCanceledOrderNum += dto.getProductInstanceNum();
            if(maxProductNum < notCanceledOrderNum){
                violation.add(new EntityConstraintViolation(String.format("Przekroczono maksymalną ilość produktów (%s) dla jednej umowy - "
                        + "ilość po dodaniu danego zamówienia: %s", maxProductNum, notCanceledOrderNum)));
            }
        }
    }

    private String getZipCodeStr(ZipCode zipcode){
        return String.format("%s %s", zipcode.getZipCode(), zipcode.getCityName());
    }

    private void setAddresses(CreateOrderDTO dto, String addressInvoice, ZipCode zipCodeInvoice,
                                                  String addressCorr, ZipCode zipCodeCorr){
        if(addressInvoice != null){
            dto.setAddressInvoice(addressInvoice);
        }
        if(zipCodeInvoice != null){
            dto.setZipCodeInvoice(zipCodeEntityMapper.convert(zipCodeInvoice));
        }
        if(addressCorr != null){
            dto.setAddressCorr(addressCorr);
        }
        if(zipCodeCorr != null){
            dto.setZipCodeCorr(zipCodeEntityMapper.convert(zipCodeCorr));
        }
    }



}
