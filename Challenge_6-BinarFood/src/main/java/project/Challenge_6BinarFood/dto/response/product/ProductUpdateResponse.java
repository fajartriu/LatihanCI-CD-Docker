package project.Challenge_6BinarFood.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateResponse {
    private UUID id;
    private UUID merchantId;
    private String productName;
    private Double price;
    private boolean deleteProduct;
}
