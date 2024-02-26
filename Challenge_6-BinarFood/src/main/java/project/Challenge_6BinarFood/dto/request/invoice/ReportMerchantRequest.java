package project.Challenge_6BinarFood.dto.request.invoice;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMerchantRequest {
    private UUID id_merchant;
    @Size(max = 50)
    private String merchant_name;
    @Size(max = 50)
    private String merchant_location;
    private LocalDateTime order_time;
    private UUID id_order_detail;
    @Size(max = 50)
    private String username;
    @Size(max = 100)
    private String destination_address;
    private UUID id_product;
    @Size(max = 50)
    private String product_name;
    private boolean completed;
    private Double price;
    private Integer quantity;
    private Double total_price;
}
