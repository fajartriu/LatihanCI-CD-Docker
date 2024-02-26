package project.Challenge_6BinarFood.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class JwtResponseForgotPassword {
    private String message;
    private String otp;

}
