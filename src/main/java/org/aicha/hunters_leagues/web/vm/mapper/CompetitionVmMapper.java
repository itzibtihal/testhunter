package org.aicha.hunters_leagues.web.vm.mapper;

import org.aicha.hunters_leagues.domain.Competition;
import org.aicha.hunters_leagues.web.vm.request.CompetitionRequest;
import org.aicha.hunters_leagues.web.vm.response.CompetitionResponse;
import org.aicha.hunters_leagues.web.vm.response.CompetitionResultsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompetitionVmMapper {

    Competition toCompetition(CompetitionRequest competitionRequest);


    CompetitionResponse toCompetitionResponse(Competition competition);

    List<CompetitionResultsResponse> toCompetitionResultsResponse(List<Competition> competitions);





}
