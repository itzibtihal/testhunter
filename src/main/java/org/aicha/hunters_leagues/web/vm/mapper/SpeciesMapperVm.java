package org.aicha.hunters_leagues.web.vm.mapper;

import org.aicha.hunters_leagues.domain.Species;
import org.aicha.hunters_leagues.web.vm.request.SerchByCategorySpeciesRequest;
import org.aicha.hunters_leagues.web.vm.request.SpeciesRequest;
import org.aicha.hunters_leagues.web.vm.response.SpeciesResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SpeciesMapperVm {

    Species toSpecies(SpeciesRequest speciesRequest);

    Species toSpecies(SerchByCategorySpeciesRequest serchByCategorySpeciesRequest);

    SpeciesResponse toSpeciesResponse(Species species);

    List<SpeciesResponse> toSpeciesResponseList(Page<Species> speciesList);

}