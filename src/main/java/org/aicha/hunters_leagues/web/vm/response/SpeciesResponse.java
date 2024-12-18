package org.aicha.hunters_leagues.web.vm.response;

import org.aicha.hunters_leagues.domain.enums.SpeciesType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SpeciesResponse {

    private UUID id;

    private String name;

    private SpeciesType category;
}
