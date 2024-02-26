package project.Challenge_6BinarFood.dto.request.user;

import lombok.*;
import project.Challenge_6BinarFood.model.user.ERole;
import project.Challenge_6BinarFood.model.user.Role;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String role;
}
