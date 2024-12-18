package org.aicha.hunters_leagues.web.vm.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class UserHistoryResponse {

    private UUID id;
    private LocalDateTime date;
    private String location;
    private Double score;
    private Integer rank;


}
