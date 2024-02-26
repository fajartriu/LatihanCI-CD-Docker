package project.Challenge_6BinarFood.dto.request.merchant;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMerchantRequest {
    @Size(max = 50)
    private String merchantName;
    @Size(max = 50)
    private String merchantLocation;
    private boolean open;
}