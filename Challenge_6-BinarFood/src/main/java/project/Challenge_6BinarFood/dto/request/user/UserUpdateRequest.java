package project.Challenge_6BinarFood.dto.request.user;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String role;
}
