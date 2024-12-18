package org.aicha.hunters_leagues.web.vm.response;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class CompetitionResponse {


    private UUID id;

    private String location;

    private LocalDateTime date;

    private Integer participationCount = 0;








}
