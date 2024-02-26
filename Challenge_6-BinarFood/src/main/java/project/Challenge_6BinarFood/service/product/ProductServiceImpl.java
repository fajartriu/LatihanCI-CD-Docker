package project.Challenge_6BinarFood.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Challenge_6BinarFood.dto.request.product.CreateProductRequest;
import project.Challenge_6BinarFood.dto.response.product.ProductResponse;
import project.Challenge_6BinarFood.dto.response.product.ProductUpdateResponse;
import project.Challenge_6BinarFood.model.merchant.Merchant;
import project.Challenge_6BinarFood.model.product.Product;
import project.Challenge_6BinarFood.repository.merchant.MerchantRepository;
import project.Challenge_6BinarFood.repository.product.ProductRepository;
import project.Challenge_6BinarFood.service.validation.ValidationService;

import java.util.*;
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public ProductResponse createProduct(UUID merchantId, CreateProductRequest request) {
        try {
            validationService.validate(request);
            Merchant merchant = merchantRepository.findById(merchantId)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant is not found"));

            Product product = new Product();
            product.setId(UUID.randomUUID());
            product.setMerchant(merchant);
            product.setProductName(request.getProductName());
            product.setPrice(request.getPrice());
            product.setDeleteProduct(true);
            productRepository.save(product);
            logger.info("Success store data product");

            return ProductResponse.builder()
                    .id(product.getId())
                    .merchantId(product.getMerchant().getId())
                    .merchantName(merchant.getMerchantName())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .build();
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ProductUpdateResponse updateDetailProduct(UUID merchantId, UUID productId, Map<String, Optional> field) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
            for (Map.Entry<String, Optional> m : field.entrySet()){
                String keyFromMap = m.getKey();

                if (keyFromMap.equals("productName")){
                    String obj = m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1");
                    logger.debug(obj);
                    product.setProductName(obj);
                }

                if (keyFromMap.equals("price")){
                    Double obj = Double.valueOf(m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1"));
                    logger.debug(obj.toString());
                    product.setPrice(obj);
                }

                if (keyFromMap.equals("deleteProduct")){
                    boolean obj = Boolean.parseBoolean(m.getValue().toString().replaceAll("^Optional\\[(.*?)\\]$", "$1"));
                    logger.debug(String.valueOf(obj));
                    product.setDeleteProduct(obj);
                }
            }

            productRepository.save(product);
            logger.info("Success store data product");

            return ProductUpdateResponse.builder()
                    .id(product.getId())
                    .merchantId(product.getMerchant().getId())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .deleteProduct(product.isDeleteProduct())
                    .build();
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> all = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        if (!all.isEmpty()){
            for (Product product: all){
                Merchant merchant = merchantRepository.findById(product.getMerchant().getId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
                productResponses.add(new ProductResponse(product.getId(), product.getMerchant().getId(), merchant.getMerchantName(), product.getProductName(), product.getPrice()));
                logger.debug(productResponses.toString());
            }
        }

        if (!productResponses.isEmpty()){
            logger.info("Data response success " + productResponses);
            return productResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public void deleteProduct(UUID merchantId,UUID productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(()
                    -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
            productRepository.delete(product);
            logger.info("Delete product success : " + productId);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    @Override
    public List<ProductResponse> paginationWithSorting(int page, int pageSize, String filter) {
        Page<Product> allPagination = productRepository.findAll(PageRequest.of(page, pageSize, Sort.by(filter).ascending()));
        List<ProductResponse> productResponses = new ArrayList<>();

        if (!allPagination.isEmpty()){
            for (Product product: allPagination){
                Merchant merchant = merchantRepository.findById(product.getMerchant().getId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
                productResponses.add(new ProductResponse(product.getId(), product.getMerchant().getId(), merchant.getMerchantName(), product.getProductName(), product.getPrice()));
                logger.debug(productResponses.toString());
            }
        }

        if (!productResponses.isEmpty()){
            logger.info("Data response success " + productResponses);
            return productResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public List<ProductResponse> paginationWithoutSorting(int page, int pageSize) {
        Page<Product> allPaginationSort = productRepository.findAll(PageRequest.of(page, pageSize));
        List<ProductResponse> productResponses = new ArrayList<>();
        if (!allPaginationSort.isEmpty()){
            for (Product product: allPaginationSort){
                Merchant merchant = merchantRepository.findById(product.getMerchant().getId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
                productResponses.add(new ProductResponse(product.getId(), product.getMerchant().getId(), merchant.getMerchantName(), product.getProductName(), product.getPrice()));
                logger.debug(productResponses.toString());
            }
        }

        if (!productResponses.isEmpty()){
            logger.info("Data response success " + productResponses);
            return productResponses;
        }
        logger.error("Response Data is empty: " + Collections.emptyList());
        return Collections.emptyList();
    }

    @Override
    public ProductResponse getProductById(UUID merchantId, UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
        Merchant merchant = merchantRepository.findById(product.getMerchant().getId()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
        return ProductResponse.builder()
                .id(product.getId())
                .merchantId(product.getMerchant().getId())
                .merchantName(merchant.getMerchantName())
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();
    }
}
