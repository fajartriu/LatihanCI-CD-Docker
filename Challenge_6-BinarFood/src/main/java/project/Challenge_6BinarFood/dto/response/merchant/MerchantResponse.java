package project.Challenge_6BinarFood.dto.response.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantResponse {
    private UUID id;
    private UUID userId;
    private String merchantName;
    private String merchantLocation;
    private boolean open;
}
