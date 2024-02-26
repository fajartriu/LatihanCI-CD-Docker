package project.Challenge_6BinarFood.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.Challenge_6BinarFood.dto.request.product.CreateProductRequest;
import project.Challenge_6BinarFood.dto.response.product.ProductResponse;
import project.Challenge_6BinarFood.dto.response.product.ProductUpdateResponse;
import project.Challenge_6BinarFood.dto.response.web.WebResponse;
import project.Challenge_6BinarFood.service.product.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/merchant")
public class ProductController {
    private final ProductService productService;

    @PostMapping(
            path = "/{merchantId}/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<ProductResponse> createProduct(@PathVariable UUID merchantId, @RequestBody CreateProductRequest request){
        try {
            ProductResponse product = productService.createProduct(merchantId, request);
            return WebResponse.<ProductResponse>builder().countRecord(1).data(product).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<ProductResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @PatchMapping(
            path = "/{merchantId}/products/{productId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<ProductUpdateResponse> updateMerchant(@PathVariable UUID merchantId, @PathVariable UUID productId, @RequestBody Map<String, Optional> field){
        try {
            ProductUpdateResponse productResponse = productService.updateDetailProduct(merchantId, productId, field);
            return WebResponse.<ProductUpdateResponse>builder().countRecord(1).data(productResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<ProductUpdateResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/{merchantId}/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<ProductResponse> getProductById(@PathVariable UUID merchantId,@PathVariable UUID productId){
        try {
            ProductResponse product = productService.getProductById(merchantId, productId);
            return WebResponse.<ProductResponse>builder().countRecord(1).data(product).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<ProductResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<ProductResponse>> getAllProduct(){
        try {
            List<ProductResponse> allProduct = productService.getAllProduct();
            return WebResponse.<List<ProductResponse>>builder().countRecord(allProduct.size()).data(allProduct).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<ProductResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/products/pagination/{page}/{pageSize}/{filter}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<ProductResponse>> paginationWithSorting(@PathVariable int page, @PathVariable int pageSize, @PathVariable String filter){
        try {
            List<ProductResponse> paginationWithSortingResponse = productService.paginationWithSorting(page, pageSize, filter);
            return WebResponse.<List<ProductResponse>>builder().countRecord(paginationWithSortingResponse.size()).data(paginationWithSortingResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<ProductResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/products/pagination/{page}/{pageSize}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<ProductResponse>> paginationWithoutSorting(@PathVariable int page, @PathVariable int pageSize){
        try {
            List<ProductResponse> paginationWithoutSortingResponse = productService.paginationWithoutSorting(page, pageSize);
            return WebResponse.<List<ProductResponse>>builder().countRecord(paginationWithoutSortingResponse.size()).data(paginationWithoutSortingResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<ProductResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @DeleteMapping(
            path = "/{merchantId}/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<String> deleteProduct(@PathVariable UUID merchantId, @PathVariable UUID productId){
        try {
            productService.deleteProduct(merchantId, productId);
            return WebResponse.<String>builder().data("Deleted : "+ productId).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<String>builder().data(null).messages(e.getMessage()).build();
        }
    }
}

