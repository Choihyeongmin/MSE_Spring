package rph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import rph.entity.Ranking;

import java.util.List;

import java.util.Optional;
import rph.entity.User;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findAllByOrderByTotalPointsDesc();

    // native query 또는 limit query로 Top N 가져오기
    Page<Ranking> findAllByOrderByTotalPointsDesc(Pageable pageable); 
    Optional<Ranking> findByUser(User user);
}
