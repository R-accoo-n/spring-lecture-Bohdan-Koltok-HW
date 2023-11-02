package co.inventorsoft.academy.spring;

import co.inventorsoft.academy.spring.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
