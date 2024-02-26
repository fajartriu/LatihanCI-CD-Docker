package project.Challenge_6BinarFood.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class JwtResponseRegister {
    private String token;
    private String type;
    private String username;
//    private String roles;
    private List<String> roles;
    private String otp;
    private LocalDateTime otpGeneratedTime;

    public JwtResponseRegister(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
