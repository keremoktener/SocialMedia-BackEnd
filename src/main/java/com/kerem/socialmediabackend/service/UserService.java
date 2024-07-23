package com.kerem.socialmediabackend.service;

import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.FindAllByUsernameRequestDto;
import com.kerem.socialmediabackend.dto.request.UserLoginRequestDto;
import com.kerem.socialmediabackend.dto.request.UserSaveRequestDto;
import com.kerem.socialmediabackend.dto.response.SearchUserResponseDto;
import com.kerem.socialmediabackend.entity.Follow;
import com.kerem.socialmediabackend.entity.User;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.repository.UserRepository;
import com.kerem.socialmediabackend.view.VwSearchUser;
import com.kerem.socialmediabackend.view.VwUserAvatar;
import com.kerem.socialmediabackend.view.VwUserForFollow;
import com.kerem.socialmediabackend.view.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtManager jwtManager;
    private final UserRepository userRepository;
    private final FollowService followService;

    public User save(UserSaveRequestDto dto) {
        return userRepository.save(User.builder()
                .password(dto.getPassword())
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .build());
    }

    public Optional<User> login(UserLoginRequestDto dto) {
        return userRepository.findOptionalByUserNameAndPassword(dto.getUserName(), dto.getPassword());
    }

    public List<SearchUserResponseDto> search(String userName) {
        List<User> userList;
        List<SearchUserResponseDto> result = new ArrayList<>();
        if (Objects.isNull(userName))
            userList = userRepository.findAll();
        else
            userList = userRepository.findAllByUserNameContaining(userName);
        userList.forEach(u ->
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
        if (authId.isEmpty()) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        return userRepository.getByAuthId(authId.get());
    }

    public void editProfile(User user) {
        userRepository.save(user);
    }

    public List<VwUserAvatar> getUserAvatarList() {
        return userRepository.getAllUserAvatar();
    }

    public List<User> findAllbyId(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public HashMap<Long, User> findAllbyIdMap(List<Long> ids) { // olmadı
        return userRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(
                        User::getId, // User nesnesinin id'sini almak için
                        user -> user, // User nesnesini kendisi olarak eklemek için
                        (existing, replacement) -> existing, // Çakışan id'ler varsa, mevcut değeri kullanmak için
                        HashMap::new // Sonuçta elde edilecek Map'in tipi
                ));
    }

    public Map<Long, User> findAllByIdMap(List<Long> ids) {
        List<User> userList = userRepository.findAllById(ids);
        Map<Long, User> result = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return result;
    }

    public VwUserAvatar getUserAvatarAndUserName(Long userId) {
        return userRepository.getUserAvatarAndUserName(userId);
    }

    public List<VwUserForFollow> getUserNotFollowed(String token) {
        Long userId = jwtManager.getAuthId(token).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        List<VwUserForFollow> usersNotFollowed = userRepository.findUsersNotFollowedBy(followService.findFollowedUserIds(userId));


        usersNotFollowed.removeIf(vwUserForFollow -> vwUserForFollow.getId().equals(userId));

        return usersNotFollowed;
    }


    public List<VwSearchUser> getAllByUserName(FindAllByUsernameRequestDto dto) {
        Optional<Long> authIdOpt = jwtManager.getAuthId(dto.getToken());
        if(authIdOpt.isEmpty()) throw new AuthException(ErrorType.INVALID_TOKEN);
        Long authId = authIdOpt.get();

        List<VwSearchUser> allUsers = userRepository.getAllByUserName("%" + dto.getUserName() + "%");
        List<Follow> followedUsersList = followService.findAllByUserId(authId);
        Set<Long> followedUserIds = followedUsersList.stream()
                .map(Follow::getFollowId)
                .collect(Collectors.toSet());

        allUsers.removeIf(u -> followedUserIds.contains(u.getId()));

        // Kendi kullanıcınızı almak için bir kere sorgu yapın
        Optional<User> currentUserOpt = userRepository.findById(authId);
        if (currentUserOpt.isPresent()) {
            String currentUserName = currentUserOpt.get().getUserName();
            // Kendi kullanıcınızı silme
            allUsers.removeIf(u -> u.getUserName().equals(currentUserName));
        }

        return allUsers;
    }


}
