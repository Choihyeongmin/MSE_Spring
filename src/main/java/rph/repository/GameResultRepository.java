package rph.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rph.entity.GameResult;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByPlayer1_IdOrPlayer2_Id(Long id1, Long id2);
    List<GameResult> findByPlayer1_IdAndPlayer2_Id(Long id1, Long id2);
}