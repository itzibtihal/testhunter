package org.aicha.hunters_leagues.mapper;

import org.aicha.hunters_leagues.domain.Competition;
import org.aicha.hunters_leagues.repository.dto.CompetitionRepoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompetitionDTOMapper {

    CompetitionRepoDTO toCompetitionDTO(Competition competition);

    List<CompetitionRepoDTO> toCompetitionDTO(List<CompetitionRepoDTO> content);
}
