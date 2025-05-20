package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import rph.entity.Ranking;

import java.util.List;

import java.util.Optional;
import rph.entity.User;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findAllByOrderByTotalPointsDesc();

    Page<Ranking> findAllByOrderByTotalPointsDesc(Pageable pageable); 
    Optional<Ranking> findByUser(User user);
}
