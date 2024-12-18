package org.aicha.hunters_leagues.web.vm.request;

import org.aicha.hunters_leagues.domain.enums.SpeciesType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerchByCategorySpeciesRequest {
    @Enumerated(EnumType.STRING)
    private SpeciesType category;
}
