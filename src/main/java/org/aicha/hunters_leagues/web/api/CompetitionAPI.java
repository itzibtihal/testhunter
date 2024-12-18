package org.aicha.hunters_leagues.web.api;

import org.aicha.hunters_leagues.domain.Competition;

import org.aicha.hunters_leagues.repository.dto.CompetitionRepoDTO;
import org.aicha.hunters_leagues.service.CompetitionService;
import org.aicha.hunters_leagues.web.vm.mapper.CompetitionVmMapper;
import org.aicha.hunters_leagues.web.vm.request.CompetitionRequest;
import org.aicha.hunters_leagues.web.vm.response.CompetitionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionAPI {

    private final CompetitionVmMapper competitionVmMapper;
    private final CompetitionService competitionService;


    @PostMapping
    public ResponseEntity<CompetitionResponse> addCompetition(
            @Valid @RequestBody CompetitionRequest competitionRequest) {

        Competition competitionEntity = competitionVmMapper.toCompetition(competitionRequest);
        Competition competition = competitionService.addCompetition(competitionEntity);

        return ResponseEntity.ok(competitionVmMapper.toCompetitionResponse(competition));
    }

    @GetMapping

    public ResponseEntity<Page<CompetitionRepoDTO>> getCompetitions(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CompetitionRepoDTO> competitionDTOs = competitionService.getAllCompetition(pageable);
        return ResponseEntity.ok(competitionDTOs);
    }


}