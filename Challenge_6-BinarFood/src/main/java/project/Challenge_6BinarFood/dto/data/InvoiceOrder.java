package project.Challenge_6BinarFood.dto.data;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class InvoiceOrder {
    private UUID orderId;
    private LocalDateTime orderTime;
    @Size(max = 50)
    private String username;
    @Size(max = 50)
    private String productName;
    @Size(max = 50)
    private String merchantName;
    @Size(max = 100)
    private String destinationAddress;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    private boolean completed;
}
