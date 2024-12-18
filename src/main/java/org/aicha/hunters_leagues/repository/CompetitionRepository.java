package org.aicha.hunters_leagues.repository;

import org.aicha.hunters_leagues.domain.Competition;
import org.aicha.hunters_leagues.repository.dto.CompetitionRepoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
//test
    Page<Competition> findAll(Pageable pageable);

    @Query("SELECT new org.aicha.hunters_leagues.repository.dto.CompetitionRepoDTO(" +
            "c.id, c.location, c.date, SIZE(c.participations)) " +
            "FROM Competition c")
    Page<CompetitionRepoDTO> findAllRepoDTO(Pageable pageable);



    @Query("SELECT c FROM Competition c WHERE c.date BETWEEN :startOfWeek AND :endOfWeek")
    Optional<Competition> findCompetitionByDateRange(@Param("startOfWeek") LocalDateTime startOfWeek,
                                                     @Param("endOfWeek") LocalDateTime endOfWeek);




}
