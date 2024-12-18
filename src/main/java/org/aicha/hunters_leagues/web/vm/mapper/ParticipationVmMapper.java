package org.aicha.hunters_leagues.web.vm.mapper;

import org.aicha.hunters_leagues.domain.Participation;

import org.aicha.hunters_leagues.web.vm.request.ParticipationRequest;
import org.aicha.hunters_leagues.web.vm.response.CompetitionResultsResponse;
import org.aicha.hunters_leagues.web.vm.response.ParticipationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationVmMapper {

    ParticipationVmMapper INSTANCE = Mappers.getMapper(ParticipationVmMapper.class);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "competition.id", source = "competitionId")
    Participation toParticipation(ParticipationRequest participationRequest);


    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "code", source = "competition.code")
    ParticipationResponse toParticipationResponse(Participation participation);
    List<CompetitionResultsResponse> toParticipationResultResponse(List<Participation> participations);
}

