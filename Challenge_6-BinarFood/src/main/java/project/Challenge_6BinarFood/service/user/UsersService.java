package project.Challenge_6BinarFood.service.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import project.Challenge_6BinarFood.dto.response.user.UsersUpdateResponse;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UsersService {

    @Transactional
    void deleteUser(Principal principal);

    UsersUpdateResponse updateUser(Principal principal, Map<String, Optional> field);
}
