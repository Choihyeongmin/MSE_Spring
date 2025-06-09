package rph.repository;

import rph.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    User findByUsername(String username);
    User findByGoogleId(String googleId);
}