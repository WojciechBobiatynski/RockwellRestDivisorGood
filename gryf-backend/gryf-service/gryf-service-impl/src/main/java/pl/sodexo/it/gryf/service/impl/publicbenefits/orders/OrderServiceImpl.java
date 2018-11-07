package pl.sodexo.it.gryf.service.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.OrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.InvalidObjectIdException;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderElementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransitionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.OrderFlowTransitionDTOProvider;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.searchform.OrderEntityToSearchResultMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    //PRIVATE FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrderElementRepository orderElementRepository;

    @Autowired
    protected OrderServiceLocal orderServiceLocal;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private OrderFlowStatusTransitionRepository orderFlowStatusTransitionRepository;

    @Autowired
    private OrderEntityToSearchResultMapper orderEntityToSearchResultMapper;

    @Autowired
    OrderFlowStatusRepository orderFlowStatusRepository;

    @Autowired
    DictionaryEntityMapper dictionaryEntityMapper;

    //PUBLIC METHODS

    @Override
    public CreateOrderDTO createCreateOrderDTO(String contractId){
        Contract contract = contractRepository.get(contractId);
        return orderServiceLocal.createCreateOrderDTO(contract);
    }


    @Override
    public Long createOrder(CreateOrderDTO dto){
        Order order = orderServiceLocal.createOrder(dto);
        return order.getId();
    }

    @Override
    public String getOrderDataToModify(Long id) {

        Order order = orderRepository.get(id);
        if (order == null){
            throw new InvalidObjectIdException("Nie znaleziono zamówienia o id " + id);
        }
        List<OrderElementDTOBuilder> orderElementDTOBuilders = orderElementRepository.findDtoFactoryByOrderToModify(id);
        List<OrderFlowTransitionDTO> orderFlowStatusTransitions = orderFlowStatusTransitionRepository.findDtoByOrder(id).stream().map(orderFlowTransitionDTOBuilder -> OrderFlowTransitionDTOProvider
                .createOrderFlowTransitionDTO(orderFlowTransitionDTOBuilder)).collect(Collectors.toList());
        List<OrderElementDTO> elements = orderServiceLocal.createOrderElementDtolist(orderElementDTOBuilders);

        OrderDTO orderDTO = OrderDTO.create(order.getId(), elements, orderFlowStatusTransitions, order.getVersion());
        return JsonMapperUtils.writeValueAsString(orderDTO);
    }

    @Override
    public String getOrderDataToPreview(Long id) {

        Order order = orderRepository.get(id);
        if (order == null){
            throw new InvalidObjectIdException("Nie znaleziono zamówienia o id " + id);
        }        
        List<OrderElementDTOBuilder> orderElementDTOBuilders = orderElementRepository.findDtoFactoryByOrderToPreview(id);
        List<OrderFlowTransitionDTO> orderFlowStatusTransitions = new ArrayList<>();
        List<OrderElementDTO> elements = orderServiceLocal.createOrderElementDtolist(orderElementDTOBuilders);

        OrderDTO orderDTO = OrderDTO.create(order.getId(), elements, orderFlowStatusTransitions, order.getVersion());
        return JsonMapperUtils.writeValueAsString(orderDTO);
    }

    @Override
    public List<OrderSearchResultDTO> findOrders(OrderSearchQueryDTO searchDTO) {
        List<Object[]> orders = orderRepository.findOrders(searchDTO);
        return orderEntityToSearchResultMapper.convertFromObjects(orders);
    }

    @Override
    public FileDTO getOrderAttachmentFile(Long elementId) {
        OrderElement orderElement = orderElementRepository.get(elementId);
        FileDTO dto = new FileDTO();
        dto.setName(GryfStringUtils.findFileNameInPath(orderElement.getValueVarchar()));
        dto.setInputStream(fileService.getInputStream(orderElement.getValueVarchar()));
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        Order order = orderRepository.get(id);
        throw new StaleDataException(order.getId(), order);
    }

    //PRIVATE METHODS

    public List<DictionaryDTO> getOrderFlowStatusesByGrantProgram(Long grantProgramId) {
        List<OrderFlowStatus> orderFlowStatuses = orderFlowStatusRepository.findByGrantProgram(grantProgramId);
        return dictionaryEntityMapper.convert(orderFlowStatuses);
    }

}
