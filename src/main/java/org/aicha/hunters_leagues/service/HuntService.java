package org.aicha.hunters_leagues.service;

import org.aicha.hunters_leagues.domain.Hunt;
import org.aicha.hunters_leagues.domain.Participation;
import org.aicha.hunters_leagues.domain.Species;
import org.aicha.hunters_leagues.repository.HuntRepository;
import org.aicha.hunters_leagues.web.vm.request.HuntRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class HuntService {

    private HuntRepository huntRepository;
    private ParticipationService participationService;
    private SpeciesService speciesService;

    @Autowired
    public HuntService(HuntRepository huntRepository, @Lazy ParticipationService participationService, SpeciesService speciesService) {
        this.huntRepository = huntRepository;
        this.participationService = participationService;
        this.speciesService = speciesService;
    }

    @Transactional
    public void deleteBySpecies(UUID id) {
        huntRepository.deleteBySpeciesId(id);
    }

    public void deleteHuntsByParticipation(Participation participation) {
        huntRepository.deleteHuntsByParticipation(participation);
    }

    public void deleteByParticipations(List<Participation> participations) {
        huntRepository.deleteByParticipations(participations);
    }

    public double registerHunt(HuntRequest huntRequest) {

        Participation participation = participationService.findById(huntRequest.getParticipationId());
        Species species = speciesService.findById(huntRequest.getSpeciesId());

        Hunt hunt = Hunt.builder()
                .species(species)
                .participation(participation)
                .weight(huntRequest.getWeight())
                .build();

        huntRepository.save(hunt);

        if (species.getMinimumWeight() <= hunt.getWeight()) {
            return participationService.updateScore(participation);
        }
        return participation.getScore();
    }

    public List<Hunt> findByParticipation(Participation participation) {
        return huntRepository.findByParticipation(participation);
    }
}