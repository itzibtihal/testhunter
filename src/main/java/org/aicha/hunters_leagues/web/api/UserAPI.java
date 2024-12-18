package org.aicha.hunters_leagues.web.api;

import org.aicha.hunters_leagues.domain.User;
import org.aicha.hunters_leagues.web.vm.request.AuthRequest;
import org.aicha.hunters_leagues.web.vm.request.UserRequest;
import org.aicha.hunters_leagues.web.vm.request.UserSearchRequest;
import org.aicha.hunters_leagues.service.UserService;
import org.aicha.hunters_leagues.web.vm.mapper.UserVmMapper;

import org.aicha.hunters_leagues.web.vm.response.ErrorResponse;
import org.aicha.hunters_leagues.web.vm.response.UserHistoryResponse;
import org.aicha.hunters_leagues.web.vm.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final UserVmMapper userVmMapper;


    @GetMapping
    public ResponseEntity<List<UserResponse>> searchUsers(@Valid UserSearchRequest userSearchRequest ,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nationality").ascending());
        User user = userVmMapper.toUser(userSearchRequest);
        Page<User> users = userService.searchUsers(user,pageable);

        List<UserResponse> usersDTO = userVmMapper.toUsersResponceList(users);

        return ResponseEntity.ok(usersDTO);
    }


    @PostMapping
    public ResponseEntity<UserResponse> addUser(
            @Valid @RequestBody UserRequest userRequest) {
        User userEntity = userVmMapper.toUser(userRequest);
        User user = userService.addSUser(userEntity);
        return ResponseEntity.ok(userVmMapper.toUserResponse(user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest userRequest) {

        User userEntity = userVmMapper.toUser(userRequest);

        User user = userService.updateUser(id, userEntity);
        return ResponseEntity.ok(userVmMapper.toUserResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteSpeciesById(@PathVariable UUID id) {
        User user = new User();
        user.setId(id);
        User deletedUser = userService.deleteUser(user);
        return ResponseEntity.ok(userVmMapper.toUserResponse(deletedUser));
    }


    @GetMapping("/history/{id}")
    public Page<UserHistoryResponse> getUserCompetitionHistory(@PathVariable UUID id,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("competition.date").descending());
        return userService.getUserCompetitionHistory(id, pageable);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {

        try {
            return ResponseEntity.ok(userService.loginAuth(authRequest));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
        
    }

}
