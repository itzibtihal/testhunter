package org.aicha.hunters_leagues.web.vm.mapper;

import org.aicha.hunters_leagues.domain.User;
import org.aicha.hunters_leagues.web.vm.request.LoginRequest;
import org.aicha.hunters_leagues.web.vm.request.RegisterRequest;
import org.aicha.hunters_leagues.web.vm.request.UserRequest;
import org.aicha.hunters_leagues.web.vm.request.UserSearchRequest;
import org.aicha.hunters_leagues.web.vm.response.UserHistoryResponse;
import org.aicha.hunters_leagues.web.vm.response.UserResponse;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserVmMapper {



    User toUser(UserSearchRequest userSearchRequest);
    User toUser(LoginRequest loginRequest);
    User toUser(UserRequest userRequest);
    User toUser(RegisterRequest registerRequest);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUsersResponceList(Page<User> users);
    List<UserHistoryResponse> toUserHistoryResponse(Page<User> users);

}

