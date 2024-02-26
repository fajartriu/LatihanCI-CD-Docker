package project.Challenge_6BinarFood.dto.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    private String newPassword;
    private String confirmationPassword;
}
