package pl.sodexo.it.gryf.service.impl.publicbenefits.orders.orderflows;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.utils.LoginUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramProductRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;

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
    private ValidateService validateService;
            
    //PUBLIC METHODS

    @Override
    public Order createOrder(GrantApplication grantApplication, OrderFlow orderFlow){
        GrantApplicationBasicData basicData = grantApplication.getBasicData();

        validateCreateOrder(grantApplication);
        
        Order order = new Order();
        order.setId(grantApplication.getId());
        order.setOrderFlow(orderFlow);
        order.setApplication(grantApplication);
        order.setEnterprise(grantApplication.getEnterprise());
        order.setStatus(orderFlow.getInitialStatus());
        order.setOrderDate(grantApplication.getReceiptDate());
        order.setAddressCorr(basicData.getAddressCorr());
        order.setZipCodeCorrId((basicData.getZipCodeCorr() != null) ? basicData.getZipCodeCorr().getId() : null);
        order.setOperator(LoginUtils.getLogin());
        order.setProduct(findGrantProgramProduct(grantApplication.getProgram().getId(),grantApplication.getReceiptDate()).getProduct());
        
        orderRepository.save(order);
        return order;
    }
    
  /**
     * Metoda wyszukuje obiekt GrantProgramProduct na podstawie id programu doginansowania i daty wpłynięcia wniosku. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu GrantProgramProduct dla zadanych parametrów. .
     * @param 
     */
    private GrantProgramProduct findGrantProgramProduct(Long grantProgramId, Date date){
        List<GrantProgramProduct> GrantProgramProducts = grantProgramProductRepository.findByGrantProgramInDate(grantProgramId, date);
        if(GrantProgramProducts.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Nie znaleziono żadnego produktu dla programu [%s] obowiązującego dnia [%s]", grantProgramId, date));
        }
        if(GrantProgramProducts.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_PRODUCTS. Dla danej programu [%s] znaleziono więcej niż jeden produkt obowiązujący dnia [%s]", grantProgramId, date));
        }
        return GrantProgramProducts.get(0);
    }    

    /**
     * Metoda wykonuje sprawdzenia przed utworzeniem zamówienia
     * @param grantApplication wniosek o dofinansowanie
     */
    private void validateCreateOrder(GrantApplication grantApplication) {
        // wszystkie poprzednie zamówienia tego MŚP w tym programie muszą być już zakończone (w końcowych statusach)
        List <Order> orders = orderRepository.findByEnterpriseGrantProgramInNonFinalStatuses(grantApplication.getEnterprise().getId(), grantApplication.getProgram().getId());
        if (!orders.isEmpty()){
            validateService.validate("Nie można przekazać wniosku do realizacji. Znaleziono " + orders.size() + 
                                    " niezakończonych zamówień dla przedsiębiorstwa " + grantApplication.getEnterprise().getName() + " (id: " + grantApplication.getEnterprise().getId() + ") " + 
                    " w programie dofinasowania " + grantApplication.getProgram().getProgramName() +
                    ". Id niezakończonego zamówienia: " + orders.get(0).getId()) ;
       }
        
        
    }



}
