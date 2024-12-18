package org.aicha.hunters_leagues.web.vm.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RegisterResponse {
    private UUID id;
    private String email;
    private String token;

}