package project.Challenge_6BinarFood.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Challenge_6BinarFood.model.user.ERole;
import project.Challenge_6BinarFood.model.user.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByName(ERole name);
}
