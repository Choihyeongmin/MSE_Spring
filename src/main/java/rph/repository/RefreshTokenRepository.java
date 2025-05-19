package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rph.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}