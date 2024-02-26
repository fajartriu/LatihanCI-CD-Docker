package project.Challenge_6BinarFood.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Challenge_6BinarFood.dto.data.OrderDTO;
import project.Challenge_6BinarFood.dto.request.order.OrderRequest;
import project.Challenge_6BinarFood.dto.response.order.OrderResponse;
import project.Challenge_6BinarFood.model.merchant.Merchant;
import project.Challenge_6BinarFood.model.order.Order;
import project.Challenge_6BinarFood.model.order.OrderDetail;
import project.Challenge_6BinarFood.model.product.Product;
import project.Challenge_6BinarFood.model.user.User;
import project.Challenge_6BinarFood.repository.merchant.MerchantRepository;
import project.Challenge_6BinarFood.repository.order.OrderDetailRepository;
import project.Challenge_6BinarFood.repository.order.OrderRepository;
import project.Challenge_6BinarFood.repository.product.ProductRepository;
import project.Challenge_6BinarFood.repository.user.UserRepository;
import project.Challenge_6BinarFood.service.user_auth.AuthenticationServiceImpl;
import project.Challenge_6BinarFood.service.validation.ValidationService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final UserRepository usersRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ValidationService validationService;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional
    @Override
    public OrderResponse orderProduct(Principal userId, OrderRequest request) {
        try {
            validationService.validate(request);
            User users = authenticationService.getIdUser(userId.getName());
            Order order = new Order();
            order.setId(UUID.randomUUID());
            order.setOrder_time(LocalDate.now());
            order.setDestination_address(request.getDestinationAddress());
            order.setUser(users);
            order.setCompleted(false);
            orderRepository.save(order);
            logger.info("Success store data order");

            Product product = productRepository.findById(request.getProductID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

            Merchant merchant = merchantRepository.findById(product.getMerchant().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant is not found"));

            Double priceProduct = product.getPrice();
            logger.debug(priceProduct.toString());
            Integer quantityOrder = request.getQuantity();
            logger.debug(quantityOrder.toString());
            Double total = priceProduct * quantityOrder;
            logger.debug(total.toString());


            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(UUID.randomUUID());
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(request.getQuantity());
            orderDetail.setTotal_price(total);
            orderDetailRepository.save(orderDetail);
            logger.info("Success store data order detail");

            return OrderResponse.builder()
                    .orderTime(order.getOrder_time())
                    .productName(product.getProductName())
                    .merchantName(merchant.getMerchantName())
                    .completed(order.isCompleted())
                    .destinationAddress(order.getDestination_address())
                    .username(users.getUserName())
                    .price(product.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .totalPrice(total)
                    .build();
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<OrderResponse> getAllUserOrder(Principal userId) {
        User users = authenticationService.getIdUser(userId.getName());
        List<OrderDTO> allUserOrderDetail = orderDetailRepository.getAllUserOrder(users.getId());
        List<OrderResponse> orderResponses = new ArrayList<>();
        if (!allUserOrderDetail.isEmpty()){
            for (OrderDTO orderDTO: allUserOrderDetail){
                orderResponses.add(new OrderResponse(orderDTO.getOrderTime(), orderDTO.getUsername(), orderDTO.getProductName(), orderDTO.getMerchantName(), orderDTO.getDestinationAddress(), orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTotalPrice(), orderDTO.isCompleted()));
                logger.debug(orderResponses.toString());
            }
        }

        if (!orderResponses.isEmpty()){
            logger.info("Data response success " + orderResponses);
            return orderResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<OrderDTO> allOrderDetail = orderDetailRepository.getAllOrder();
        List<OrderResponse> orderResponses = new ArrayList<>();
        if (!allOrderDetail.isEmpty()){
            for (OrderDTO orderDTO: allOrderDetail){
                orderResponses.add(new OrderResponse(orderDTO.getOrderTime(),orderDTO.getUsername(), orderDTO.getProductName(), orderDTO.getMerchantName(), orderDTO.getDestinationAddress(), orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTotalPrice(), orderDTO.isCompleted()));
                logger.debug(orderResponses.toString());
            }
        }

        if (!orderResponses.isEmpty()){
            logger.info("Data response success " + orderResponses);
            return orderResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public List<OrderResponse> getAllOrderWithPagination(int page, int pageSize) {
        List<OrderDTO> allOrderDetailPagination = orderDetailRepository.paginationProduct(PageRequest.of(page, pageSize));
        List<OrderResponse> orderResponses = new ArrayList<>();

        if (!allOrderDetailPagination.isEmpty()){
            for (OrderDTO orderDTO: allOrderDetailPagination){
                orderResponses.add(new OrderResponse(orderDTO.getOrderTime(),orderDTO.getUsername(), orderDTO.getProductName(), orderDTO.getMerchantName(), orderDTO.getDestinationAddress(), orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTotalPrice(), orderDTO.isCompleted()));
                logger.debug(orderResponses.toString());
            }
        }

        if (!orderResponses.isEmpty()){
            logger.info("Data response success " + orderResponses);
            return orderResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public List<OrderResponse> getAllOrderWithPaginationSort(int page, int pageSize, String filter) {
        List<OrderDTO> allOrderDetailPaginationSort = orderDetailRepository.paginationProduct(PageRequest.of(page, pageSize, Sort.by(filter)));
        List<OrderResponse> orderResponses = new ArrayList<>();
        if (!allOrderDetailPaginationSort.isEmpty()){
            for (OrderDTO orderDTO: allOrderDetailPaginationSort){
                orderResponses.add(new OrderResponse(orderDTO.getOrderTime(),orderDTO.getUsername(), orderDTO.getProductName(), orderDTO.getMerchantName(), orderDTO.getDestinationAddress(), orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTotalPrice(), orderDTO.isCompleted()));
                logger.debug(orderResponses.toString());
            }
        }

        if (!orderResponses.isEmpty()){
            logger.info("Data response success " + orderResponses);
            return orderResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }
}

