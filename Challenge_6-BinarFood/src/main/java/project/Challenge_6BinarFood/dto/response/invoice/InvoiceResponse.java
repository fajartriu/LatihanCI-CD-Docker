package project.Challenge_6BinarFood.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private LocalDate date_order;
    private LocalTime time_order;
    private String username;
    private String product_name;
    private String merchant_name;
    private String destination_address;
    //    private String merchant_location;
    private boolean completed;
    private Double price;
    private Integer quantity;
    private Double sub_total;
    private Double total_price;
}
