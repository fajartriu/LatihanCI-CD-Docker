package project.Challenge_6BinarFood.service.order;

import org.springframework.stereotype.Service;
import project.Challenge_6BinarFood.dto.request.order.OrderRequest;
import project.Challenge_6BinarFood.dto.response.order.OrderResponse;

import java.security.Principal;
import java.util.List;

@Service
public interface OrderService {
    OrderResponse orderProduct(Principal userId, OrderRequest request);
    List<OrderResponse> getAllUserOrder(Principal userId);
    List<OrderResponse> getAllOrder();
    List<OrderResponse> getAllOrderWithPagination(int page, int pageSize);
    List<OrderResponse> getAllOrderWithPaginationSort(int page, int pageSize, String filter);
}
