package org.aicha.hunters_leagues.web.vm.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {


    @NotNull
    private String email;
    @NotNull
    private String password;
}
