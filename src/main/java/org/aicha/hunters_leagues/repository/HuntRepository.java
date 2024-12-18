package org.aicha.hunters_leagues.repository;


import org.aicha.hunters_leagues.domain.Hunt;
import org.aicha.hunters_leagues.domain.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface HuntRepository extends JpaRepository<Hunt, UUID> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Hunt h WHERE h.species.id = :speciesId")
    void deleteBySpeciesId(@Param("speciesId") UUID speciesId);

    void deleteHuntsByParticipation(Participation participation);

    @Modifying
    @Query("DELETE FROM Hunt h WHERE h.participation IN :participations")
    void deleteByParticipations(@Param("participations") List<Participation> participations);

    List<Hunt> findByParticipation(Participation participation);

}
