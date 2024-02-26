package project.Challenge_6BinarFood.service.product;

import org.springframework.stereotype.Service;
import project.Challenge_6BinarFood.dto.request.product.CreateProductRequest;
import project.Challenge_6BinarFood.dto.response.product.ProductResponse;
import project.Challenge_6BinarFood.dto.response.product.ProductUpdateResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {
    ProductResponse createProduct(UUID merchantId, CreateProductRequest request);
    ProductUpdateResponse updateDetailProduct(UUID merchantId, UUID productId, Map<String, Optional> field);
    List<ProductResponse> getAllProduct();
    void deleteProduct(UUID merchantId, UUID productId);
    List<ProductResponse> paginationWithSorting(int page, int pageSize, String filter);
    List<ProductResponse> paginationWithoutSorting(int page, int pageSize);

    ProductResponse getProductById(UUID merchantId, UUID productId);
}
