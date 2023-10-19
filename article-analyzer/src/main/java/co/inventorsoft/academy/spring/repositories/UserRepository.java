package co.inventorsoft.academy.spring.repositories;

import co.inventorsoft.academy.spring.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);

}
