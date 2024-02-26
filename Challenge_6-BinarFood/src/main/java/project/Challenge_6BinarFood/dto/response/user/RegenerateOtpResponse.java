package project.Challenge_6BinarFood.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class RegenerateOtpResponse {
    private String message;
    private String Otp;
}
