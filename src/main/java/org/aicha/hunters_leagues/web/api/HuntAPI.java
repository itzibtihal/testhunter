package org.aicha.hunters_leagues.web.api;

import org.aicha.hunters_leagues.service.HuntService;
import org.aicha.hunters_leagues.web.vm.mapper.HuntVmMapper;
import org.aicha.hunters_leagues.web.vm.request.HuntRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hunts")
@RequiredArgsConstructor
public class HuntAPI {

    private final HuntService huntService;
    private final HuntVmMapper huntVmMapper;



    @PostMapping
    public ResponseEntity<Map<String,String>> registerResult(
            @Valid @RequestBody HuntRequest huntRequest) {

           double score = huntService.registerHunt(huntRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hunt registered successfully , and the score is "+ score );
        return ResponseEntity.ok(response);
    }





}
