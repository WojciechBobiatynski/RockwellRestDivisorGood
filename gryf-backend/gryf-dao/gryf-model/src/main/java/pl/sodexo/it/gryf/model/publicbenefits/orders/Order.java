package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProduct;
import pl.sodexo.it.gryf.model.stock.products.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 * Główna encja przetrzymująca dane dla zamówienia. Wyjątkowo nazwa klasy jest inna niż nazwa encji.
 * Encja nie może nazywać sie order, ponieważ jest to słow kluczowe w jpql.
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"orderFlow", "application", "enterprise", "individual", "status", "product", "orderElements", "orderElementMap"})
@Entity(name = "OrderEntity")
@Table(name = "ORDERS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = Order.FIND_BY_ENT_PROGRAM_IN_NON_FINAL_ST, query="select o from OrderEntity o " +
                                                                            "where o.enterprise.id = :enterpriseId " +
                                                                            "and o.application.program.id = :grantProgramId " +
                                                                            "and o.status NOT IN (SELECT p.status FROM OrderFlowStatusProperty p " +
                                                                            "WHERE o.orderFlow = p.orderFlow " +
                                                                            "AND p.finalStatus = 'Y') "),
        @NamedQuery(name = Order.FIND_GRANTED_VOUCH_NUM_FOR_ENT_AND_PROGRAM, query="select SUM(o.vouchersNumber) from OrderEntity o " +
                                                                            "where o.enterprise.id = :enterpriseId " +
                                                                            "and o.application.program.id = :grantProgramId " +
                                                                            "and o.status IN (SELECT p.status FROM OrderFlowStatusProperty p " +
                                                                            "WHERE o.orderFlow = p.orderFlow " +
                                                                            "AND p.successStatus = 'Y') "),
        @NamedQuery(name = "Order.countNotCanceledOrdersByContract", query="select count(o) from OrderEntity o " +
                                                                        "where o.contract.id = :contractId and o.status not in " +
                                                                        "(SELECT p.status FROM OrderFlowStatusProperty p " +
                                                                        "WHERE o.orderFlow = p.orderFlow " +
                                                                        "AND p.finalStatus = 'Y' and p.successStatus = 'N') "),
        @NamedQuery(name = "Order.sumProductInstanceNumInNotCanceledOrdersByContract", query="select sum(o.vouchersNumber) from OrderEntity o " +
                                                                                            "where o.contract.id = :contractId " +
                                                                                            "and o.status not in " +
                                                                                            "(SELECT p.status FROM OrderFlowStatusProperty p " +
                                                                                            "WHERE o.orderFlow = p.orderFlow " +
                                                                                            "AND p.finalStatus = 'Y' and p.successStatus = 'N')"),
        @NamedQuery(name = "Order.countByGrantProgramAndExternalOrderId", query="select count(o) from OrderEntity o " +
                                                                "where o.grantProgram.id = :grantProgramId and " +
                                                                "o.externalOrderId = :externalOrderId "),




})
@OptimisticLocking(cascade=true)
@SequenceGenerator(name="order_seq", schema = "eagle", sequenceName = "order_seq", allocationSize = 1)
public class Order extends VersionableEntity {

    
    //STATIC FIELDS - ATRIBUTES
    public static final String ID_ATTR_NAME = "id";
    public static final String EXTERNAL_ORDER_ID_ATTR_NAME = "externalOrderId";
    public static final String STATUS_ATTR_NAME = "status";
    public static final String ORDER_DATE_ATTR_NAME = "orderDate";
    public static final String APPLICATION_ATTR_NAME = "application";
    public static final String GRANT_PROGRAM_ATTR_NAME = "grantProgram";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";
    public static final String CONTRACT_ATTR_NAME = "contract";
    public static final String OPERATOR_ATTR_NAME = "operator";
    public static final String ORDER_ELEMENTS_ATTR_NAME = "orderElements";

    public static final String FIND_BY_ENT_PROGRAM_IN_NON_FINAL_ST = "Order.findByEnterpriseGrantProgramInNonFinalStatuses";    
    public static final String FIND_GRANTED_VOUCH_NUM_FOR_ENT_AND_PROGRAM = "Order.findGrantedVoucherNumberForEntAndProgram";        
    
    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "order_seq")
    private Long id;

    @Column(name = "EXTERNAL_ORDER_ID")
    private String externalOrderId;

    @JsonBackReference("orders")
    @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlow orderFlow;

    @JsonBackReference("order")
    @JoinColumn(name = "GRANT_APPLICATION_ID", referencedColumnName = "ID")
    @OneToOne
    private GrantApplication application;

    @JsonBackReference("orders")
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GrantProgram grantProgram;

    @JoinColumn(name = "ENTERPRISE_ID", referencedColumnName = "ID")
    @ManyToOne()
    private Enterprise enterprise;
    
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID")
    @ManyToOne()
    private Contract contract;

    @JsonManagedReference("status")
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID")
    @ManyToOne(optional = false)
    private OrderFlowStatus status;

    @NotNull
    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "VOUCHERS_NUMBER")
    private Integer vouchersNumber;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "PBE_PRD_ID", referencedColumnName = "PRD_ID")
    private PbeProduct pbeProduct;

    @Size(max = 200)
    @Column(name = "ADDRESS_CORR")
    private String addressCorr;

    @JoinColumn(name = "ZIP_CODE_CORR_ID")
    @ManyToOne
    private ZipCode zipCodeCorr;

    @Size(max = 1000)
    @Column(name = "REMARKS")
    private String remarks;

    @Size(max = 20)
    @Column(name = "OPERATOR_ID")
    private String operator;

    @Getter
    @Setter
    @Column(name = "OWN_CONTRIBUTION_PERCENTAGE")
    private BigDecimal ownContributionPercentage;

    @JsonManagedReference("orderElements")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderElement> orderElements;

    @JsonManagedReference("orderInvoices")
    @OneToMany(mappedBy = "order")
    private List<OrderInvoice> orderInvoices;

    @Transient
    private Map<String, OrderElement> orderElementMap;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
    }

    public GrantApplication getApplication() {
        return application;
    }

    public void setApplication(GrantApplication application) {
        this.application = application;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public OrderFlowStatus getStatus() {
        return status;
    }

    public void setStatus(OrderFlowStatus status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getVouchersNumber() {
        return vouchersNumber;
    }

    public void setVouchersNumber(Integer vouchersNumber) {
        this.vouchersNumber = vouchersNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PbeProduct getPbeProduct() {
        return pbeProduct;
    }

    public void setPbeProduct(PbeProduct pbePproduct) {
        this.pbeProduct = pbePproduct;
    }

    public String getAddressCorr() {
        return addressCorr;
    }

    public void setAddressCorr(String addressCorr) {
        this.addressCorr = addressCorr;
    }

    public ZipCode getZipCodeCorr() {
        return zipCodeCorr;
    }

    public void setZipCodeCorr(ZipCode zipCodeCorr) {
        this.zipCodeCorr = zipCodeCorr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    //LIST METHODS

    private List<OrderElement> getInitializedOrderElements() {
        if (orderElements == null)
            orderElements = new ArrayList<>();
        return orderElements;
    }

    public List<OrderElement> getOrderElements() {
        return Collections.unmodifiableList(getInitializedOrderElements());
    }

    public void addOrderElement(OrderElement orderElement) {
        if (orderElement.getOrder() != null && orderElement.getOrder() != this) {
            orderElement.getOrder().getInitializedOrderElements().remove(orderElement);
        }
        if (orderElement.getId() == null || !getInitializedOrderElements().contains(orderElement)) {
            getInitializedOrderElements().add(orderElement);
        }
        orderElement.setOrder(this);
    }

    public void removeContact(OrderElement orderElement) {
        getInitializedOrderElements().remove(orderElement);
    }


    private List<OrderInvoice> getInitializedOrderInvoices() {
        if (orderInvoices == null)
            orderInvoices = new ArrayList<>();
        return orderInvoices;
    }

    public List<OrderInvoice> getOrderInvoices() {
        return Collections.unmodifiableList(getInitializedOrderInvoices());
    }

    public void addOrderInvoice(OrderInvoice orderInvoice) {
        if (orderInvoice.getOrder() != null && orderInvoice.getOrder() != this) {
            orderInvoice.getOrder().getInitializedOrderElements().remove(orderInvoice);
        }
        if (orderInvoice.getId() == null || !getInitializedOrderInvoices().contains(orderInvoice)) {
            getInitializedOrderInvoices().add(orderInvoice);
        }
        orderInvoice.setOrder(this);
    }


    //PUBLIC METHODS

    /**
     * Zwraca element o konkretnym kluczu.
     * @param elementId klucz
     * @return elemnt null jest możliwy
     */
    public OrderElement getElement(String elementId){
        if(orderElementMap == null){
            orderElementMap = createOrderElementMap();
        }
        return orderElementMap.get(elementId);
    }

    /**
     * Zwraca element o konkretnym kluczu. Wyrzuca wyjątek gdy element nie istnieje.
     * @param elementId klucz
     * @return elemnt null NIE jest możliwy
     */
    public OrderElement loadElement(String elementId){
        OrderElement orderElement = getElement(elementId);
        if(orderElement == null){
            throw new RuntimeException("Bład konfiguracji - nie znaleziono elementu o kluczu " + elementId);
        }
        return orderElement;
    }

    /**
     * Zwraca liste elementow dla typu.
     * @param elementTypeId typ
     * @return elemnty, pusta lista jest możliwy
     */
    public List<OrderElement> getElements(final String elementTypeId){
        return GryfUtils.filter(orderElements, new GryfUtils.Predicate<OrderElement>() {
            public boolean apply(OrderElement input) {
                OrderFlowElement ofe = input.getOrderFlowElement();
                OrderFlowElementType ofet = ofe.getOrderFlowElementType();
                return elementTypeId.equals(ofet.getId());
            }
        });
    }

    /**
     * Zwraca liste elementow dla typu. Wyrzuca wyjątek gdy elementy nie istnieją.
     * @param elementTypeId typ
     * @return elemnty, pusta lista NIE jest możliwy
     */
    public List<OrderElement> loadElements(final String elementTypeId){
        List<OrderElement> orderElements = getElements(elementTypeId);
        if(GryfUtils.isEmpty(orderElements)){
            throw new RuntimeException("Bład konfiguracji - nie znaleziono elementów o typu " + elementTypeId);
        }
        return orderElements;
    }

    private Map<String, OrderElement> createOrderElementMap(){
        return GryfUtils.constructMap(orderElements, new GryfUtils.MapConstructor<String, OrderElement>() {
            public boolean isAddToMap(OrderElement input) {
                return true;
            }
            public String getKey(OrderElement input) {
                return input.getOrderFlowElement().getElementId();
            }
        });
    }

    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((Order) o).id);
    }
}
