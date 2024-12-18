package org.aicha.hunters_leagues.web.vm.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ParticipationResponse {

    private UUID id;
    private String username;
    private String code;


}
