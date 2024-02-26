package project.Challenge_6BinarFood.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMerchantResponse {
    private UUID id_merchant;
    private String merchant_name;
    private String merchant_location;
    private LocalDate date_order;
    private LocalTime time_order;
    private UUID id_order_detail;
    private String username;
    private String destination_address;
    private UUID id_product;
    private String product_name;
    private boolean completed;
    private Double price;
    private Integer quantity;
    private Double sub_total;
    private Double total_price;
}
