package org.aicha.hunters_leagues.service;

import org.aicha.hunters_leagues.domain.*;
import org.aicha.hunters_leagues.exception.exps.LicenseExpirationDateException;
import org.aicha.hunters_leagues.exception.exps.MaxParticipantsException;
import org.aicha.hunters_leagues.exception.exps.RegistrationClosedException;
import org.aicha.hunters_leagues.exception.exps.ResourceNotFoundException;
import org.aicha.hunters_leagues.repository.ParticipationRepository;
import org.aicha.hunters_leagues.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ParticipationService {
    private final CompetitionService competitionService;
    private final UserRepository userService;
    private final ParticipationRepository participationRepository;
    @Lazy
    private HuntService huntService;

    @Autowired
    public ParticipationService(CompetitionService competitionService, UserRepository userService, ParticipationRepository participationRepository, @Lazy HuntService huntService) {
        this.competitionService = competitionService;
        this.userService = userService;
        this.participationRepository = participationRepository;
        this.huntService = huntService;
    }

    public Participation registerParticipant(@Valid Participation participation) {
        Competition competition = competitionService.findById(participation.getCompetition().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

        User user = userService.findById(participation.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getLicenseExpirationDate().isBefore(LocalDateTime.now())) {
            throw new LicenseExpirationDateException("User license is expired");
        }

        if (competition.getParticipations().size() >= competition.getMaxParticipants()) {
            throw new MaxParticipantsException("Max participants reached");
        }

        if (!competition.getOpenRegistration()) {
            throw new RegistrationClosedException("Registration is closed");
        }

        Participation newParticipation = Participation.builder()
                .user(user)
                .competition(competition)
                .score(0.0)
                .build();

        return participationRepository.save(newParticipation);
    }

    @Transactional
    public void deleteParticipationsByUser(User userToDelete) {
        participationRepository.deleteParticipationWithHunts(userToDelete.getId());
    }

    public Participation findById(UUID participationId) {
        return participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation with id '" + participationId + "' does not exist."));
    }

    public double updateScore(Participation participation) {
        List<Hunt> hunts = huntService.findByParticipation(participation);
        double totalScore = 0.0;

        for (Hunt hunt : hunts) {
            Species species = hunt.getSpecies();
            double weight = hunt.getWeight();
            totalScore += weight + (weight * species.getDifficulty().getValue()) + species.getPoints();
        }

        participation.setScore(totalScore);
        participationRepository.save(participation);

        return totalScore;
    }

    public void save(Participation participation) {
        participationRepository.save(participation);
    }

    public List<Participation> getParticipationResults(UUID userId, UUID competitionId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Competition competition = competitionService.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

        return participationRepository.findByUserAndCompetition(user, competition);
    }

    public List<Participation> getCompetitionPodium(UUID competitionId) {
        Competition competition = competitionService.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

        return participationRepository.findTop3ByCompetitionOrderByScoreDesc(competition);
    }

    public Page<Participation> findByUserOrderByCompetitionDateDesc(User user, Pageable pageable) {
        return participationRepository.findParticipationByUser(user, pageable);
    }

    public List<Participation> findByCompetitionOrderByScoreDesc(Competition competition) {
        return participationRepository.findByCompetitionOrderByScoreDesc(competition);
    }
}