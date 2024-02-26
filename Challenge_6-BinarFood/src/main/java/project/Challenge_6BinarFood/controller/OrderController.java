package project.Challenge_6BinarFood.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.Challenge_6BinarFood.dto.request.order.OrderRequest;
import project.Challenge_6BinarFood.dto.response.order.OrderResponse;
import project.Challenge_6BinarFood.dto.response.web.WebResponse;
import project.Challenge_6BinarFood.service.order.OrderService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    public WebResponse<OrderResponse> orderProduct(Principal principal, @RequestBody OrderRequest request){
        try {
            OrderResponse orderResponse = orderService.orderProduct(principal, request);
            return WebResponse.<OrderResponse>builder().countRecord(1).data(orderResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<OrderResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/getAllUserOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    public WebResponse<List<OrderResponse>> getAllUserOrder(Principal principal){
        try {
            List<OrderResponse> allUserOrder = orderService.getAllUserOrder(principal);
            return WebResponse.<List<OrderResponse>>builder().countRecord(allUserOrder.size()).data(allUserOrder).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<OrderResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/getAllOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<OrderResponse>> getAllOrder(){
        try {
            List<OrderResponse> allUserOrder = orderService.getAllOrder();
            return WebResponse.<List<OrderResponse>>builder().countRecord(allUserOrder.size()).data(allUserOrder).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<OrderResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/getAllOrderWithPagination/{page}/{pageSize}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<OrderResponse>> getAllOrderWithPagination(@PathVariable int page, @PathVariable int pageSize){
        try {
            List<OrderResponse> allUserOrder = orderService.getAllOrderWithPagination(page,pageSize);
            return WebResponse.<List<OrderResponse>>builder().countRecord(allUserOrder.size()).data(allUserOrder).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<OrderResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/getAllOrderWithPagination/{page}/{pageSize}/{filter}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<OrderResponse>> getAllOrderWithPaginationSort(@PathVariable int page, @PathVariable int pageSize, @PathVariable String filter){
        try {
            List<OrderResponse> allUserOrder = orderService.getAllOrderWithPaginationSort(page,pageSize, filter);
            return WebResponse.<List<OrderResponse>>builder().countRecord(allUserOrder.size()).data(allUserOrder).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<OrderResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

//    @GetMapping(
//            path = "/api/reportMerchantTes/{merchantId}/{date}",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public WebResponse<List<ReportMerchantResponse>> generateReportMerchant(@PathVariable UUID merchantId, @PathVariable LocalDate date){
//        try {
//            List<ReportMerchantResponse> reportMerchantResponses = invoiceService.generateReportMerchant(merchantId, date);
//            return WebResponse.<List<ReportMerchantResponse>>builder().countRecord(reportMerchantResponses.size()).data(reportMerchantResponses).messages("Success").build();
//        }catch (Exception e){
//            return WebResponse.<List<ReportMerchantResponse>>builder().data(null).messages(e.getMessage()).build();
//        }
//    }
}
