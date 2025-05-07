package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rph.entity.GameResult;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
