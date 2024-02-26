package project.Challenge_6BinarFood.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import project.Challenge_6BinarFood.model.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
//    Optional<User> findByEmail(String email);
}
