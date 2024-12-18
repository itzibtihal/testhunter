package org.aicha.hunters_leagues.service;

import org.aicha.hunters_leagues.config.JwtService;
import org.aicha.hunters_leagues.domain.Competition;
import org.aicha.hunters_leagues.domain.Participation;
import org.aicha.hunters_leagues.domain.User;
import org.aicha.hunters_leagues.exception.exps.EmailAlreadyExisteException;
import org.aicha.hunters_leagues.exception.exps.InvalidPasswordException;
import org.aicha.hunters_leagues.exception.exps.ResourceNotFoundException;
import org.aicha.hunters_leagues.repository.UserRepository;
import org.aicha.hunters_leagues.utils.PasswordUtil;

import org.aicha.hunters_leagues.web.vm.mapper.UserVmMapper;
import org.aicha.hunters_leagues.web.vm.request.AuthRequest;
import org.aicha.hunters_leagues.web.vm.response.AuthResponse;
import org.aicha.hunters_leagues.web.vm.response.UserHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
//user service
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParticipationService participationService;
    private final UserVmMapper userVmMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public Page<User> searchUsers(User user , Pageable pageable) {
        if (user.getFirstName() == null  &&
                user.getLastName() == null &&
                user.getCin() == null &&
                user.getEmail() == null ) {
            return userRepository.findAll(pageable);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<User> example = Example.of(user, matcher);

        return userRepository.findAll(example, pageable);


    }

    //method to add user and generate token for user to login after registration
    public User addSUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExisteException("Email already exists");
        }

        user.setJoinDate(LocalDateTime.now());
        user.setUsername(user.getFirstName() + user.getLastName());
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);
        System.out.println(token);

        savedUser.setToken(token);

        return userRepository.save(savedUser);
    }

    public User updateUser(UUID id, User user) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!userToUpdate.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new EmailAlreadyExisteException("Email already exists");
            }

        }

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setUsername(user.getFirstName()+user.getLastName());
        userToUpdate.setCin(user.getCin());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userToUpdate.setNationality(user.getNationality());
        userToUpdate.setLicenseExpirationDate(user.getLicenseExpirationDate());
        userToUpdate.setRole(user.getRole());

        return userRepository.save(userToUpdate);

    }

    @Transactional
    public User deleteUser(User user) {
        User userToDelete = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        participationService.deleteParticipationsByUser(userToDelete);
        userRepository.deleteUser(userToDelete.getId());
        //userRepository.deleteUserWithRelatedData(user.getId());
        return userToDelete;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

//    public User login(User user) {
//        User userEntity = userRepository.findByEmail(user.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("This email does not exist"));
//
//        if (!PasswordUtil.checkPassword(user.getPassword(), userEntity.getPassword())) {
//            throw new InvalidPasswordException("This password does not match");
//        }
//
//        String token = jwtService.generateToken(userEntity);
//        userEntity.setToken(token);
//
//        userRepository.save(userEntity);
//
//        return userEntity;
//    }
    //method to login user and generate token
public User login(String email, String password) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

    if (!PasswordUtil.checkPassword(password, user.getPassword())) {
        throw new InvalidPasswordException("Invalid password");
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    // Generate token and set it in the user object
    String token = jwtService.generateToken(user);
    user.setToken(token);

    return user;
}
    public Page<UserHistoryResponse> getUserCompetitionHistory(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<Participation> participations = participationService.findByUserOrderByCompetitionDateDesc(user, pageable);

        List<UserHistoryResponse> historyResponses = participations.stream()
                .map(participation -> {
                    int rank = calculateRank(participation.getCompetition(), participation.getScore());
                    return mapToUserHistoryResponse(participation, rank);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(historyResponses, pageable, participations.getTotalElements());
    }

    private int calculateRank(Competition competition, double userScore) {
        List<Participation> participations = participationService.findByCompetitionOrderByScoreDesc(competition);

        for (int i = 0; i < participations.size(); i++) {
            if (participations.get(i).getScore() == userScore) {
                return i + 1;
            }
        }
        return -1;
    }


    private UserHistoryResponse mapToUserHistoryResponse(Participation participation, int rank) {
        UserHistoryResponse response = new UserHistoryResponse();
        response.setId(participation.getId());
        response.setLocation(participation.getCompetition().getLocation());
        response.setDate(participation.getCompetition().getDate());
        response.setScore(participation.getScore());
        response.setRank(rank);
        return response;
    }



    public AuthResponse loginAuth(AuthRequest authRequest) {
        try {
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Email not found. Please check your email address."));

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getEmail(),
                                authRequest.getPassword()
                        )
                );
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Incorrect password. Please try again.");
            }

            var token = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .token(token)
                    .build();

        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }


}
