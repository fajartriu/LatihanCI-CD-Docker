package project.Challenge_6BinarFood.dto.request.order;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private UUID productID;
    @Size(max = 100)
    private String destinationAddress;
    private Integer quantity;
}
