package az.edu.turing.bankingservice.dao.repository;

import az.edu.turing.bankingservice.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByEmail(String email);
}
