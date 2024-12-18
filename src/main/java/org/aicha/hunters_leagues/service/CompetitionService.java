package org.aicha.hunters_leagues.service;


import org.aicha.hunters_leagues.domain.Competition;
import org.aicha.hunters_leagues.exception.exps.CompetitionAlreadyExistsException;
import org.aicha.hunters_leagues.repository.CompetitionRepository;
import org.aicha.hunters_leagues.repository.dto.CompetitionRepoDTO;
import org.aicha.hunters_leagues.mapper.CompetitionDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionDTOMapper competitionDTOMapper;

    public CompetitionService(CompetitionRepository competitionRepository, CompetitionDTOMapper competitionDTOMapper) {
        this.competitionRepository = competitionRepository;
        this.competitionDTOMapper = competitionDTOMapper;
    }

    public Competition addCompetition(Competition competition) {
        LocalDateTime competitionDate = competition.getDate();

        LocalDateTime startOfWeek = competitionDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = competitionDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Optional<Competition> existingCompetition = competitionRepository
                .findCompetitionByDateRange(startOfWeek, endOfWeek);
        if (existingCompetition.isPresent()) {
            throw new CompetitionAlreadyExistsException("Competition already exists for this week");
        }

        return competitionRepository.save(competition);
    }

    public  Page<CompetitionRepoDTO> getAllCompetition(Pageable pageable) {
        Page<CompetitionRepoDTO> competitionPage = competitionRepository.findAllRepoDTO(pageable);
        List<CompetitionRepoDTO> competitionDTOS = competitionDTOMapper.toCompetitionDTO(competitionPage.getContent());
        return new PageImpl<>(competitionDTOS, pageable, competitionPage.getTotalElements());
    }


    public Optional<Competition> findById(UUID id) {
        return competitionRepository.findById(id);
    }
}
