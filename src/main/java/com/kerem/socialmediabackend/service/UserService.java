package com.kerem.socialmediabackend.service;


import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.FindAllByUsernameRequestDto;
import com.kerem.socialmediabackend.dto.request.UserLoginRequestDto;
import com.kerem.socialmediabackend.dto.request.UserSaveRequestDto;
import com.kerem.socialmediabackend.dto.response.SearchUserResponseDto;
import com.kerem.socialmediabackend.entity.User;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.repository.UserRepository;
import com.kerem.socialmediabackend.views.VwSearchUser;
import com.kerem.socialmediabackend.views.VwUserAvatar;
import com.kerem.socialmediabackend.views.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtManager jwtManager;
    private final FollowService followService;
    public User save(UserSaveRequestDto dto) {
        return repository.save(User.builder()
                        .password(dto.getPassword())
                        .email(dto.getEmail())
                        .userName(dto.getUserName())
                .build());
    }

    public Optional<User> login(UserLoginRequestDto dto) {
       return repository.findOptionalByUserNameAndPassword(dto.getUserName(),dto.getPassword());
    }

    public List<SearchUserResponseDto> search(String userName) {
        List<User> userList;
        List<SearchUserResponseDto> result = new ArrayList<>();
        if(Objects.isNull(userName))
            userList = repository.findAll();
        else
            userList = repository.findAllByUserNameContaining(userName);
        userList.forEach(u->
            result.add(SearchUserResponseDto.builder()
                            .userName(u.getUserName())
                            .avatar(u.getAvatar())
                            .email(u.getEmail())
                    .build())
        );
        return result;
    }

    public VwUserProfile getProfileByToken(String token) {
        Optional<Long> authId = jwtManager.getAuthId(token);
        if(authId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        return repository.getByAuthId(authId.get());
    }

    public void editProfile(User user) {
        repository.save(user);
    }

    public VwUserAvatar getUserAvatar(Long id){
        return repository.getUserAvatar(id);
    }

    public List<VwUserAvatar> getUserAvatarList(){
        return repository.getUserAvatarList();
    }

    public List<User> findAllByIds(List<Long> userIds){
        return repository.findAllById(userIds);
    }

    public Map<Long,User> findAllByIdsMap(List<Long> userIds){
        List<User> userList = repository.findAllById(userIds);
        Map<Long,User> result = userList.stream().collect(
                Collectors.toMap(User::getId,u-> u)
        );
        return result;
    }

    public List<VwSearchUser> getAllByUserName(FindAllByUsernameRequestDto dto) {
        Optional<Long> userId = jwtManager.getAuthId(dto.getToken());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        List<Long> followIds = followService.findAllByUserId(userId.get());
        if (followIds.isEmpty()) followIds = List.of(0L);
        List<User> userList = repository
                .findAllByUserNameLikeAndIdNotIn("%"+dto.getUserName()+"%",followIds, PageRequest.of(0,6));
        return getVwSearchUsers(userList);
    }

    public List<VwSearchUser> getAllFollowList(List<Long> allFollowing) {
        List<User> userList = repository.findAllByIdIn(allFollowing, PageRequest.of(0,10));
        return getVwSearchUsers(userList);
    }

    private static List<VwSearchUser> getVwSearchUsers(List<User> userList) {
        List<VwSearchUser> result = new ArrayList<>();
        userList.forEach(u->{
            result.add(
                    VwSearchUser.builder()
                            .avatar(u.getAvatar())
                            .id(u.getId())
                            .name(u.getName())
                            .userName(u.getUserName())
                            .build()
            );
        });

        return result;
    }

}
