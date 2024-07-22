package com.kerem.socialmediabackend.controller;

import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.UserLoginRequestDto;
import com.kerem.socialmediabackend.dto.request.UserSaveRequestDto;
import com.kerem.socialmediabackend.dto.response.ResponseDto;
import com.kerem.socialmediabackend.dto.response.SearchUserResponseDto;
import com.kerem.socialmediabackend.entity.User;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.service.UserService;
import com.kerem.socialmediabackend.view.VwUserForFollow;
import com.kerem.socialmediabackend.view.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtManager jwtManager;

    @PostMapping("/register")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> save(@RequestBody UserSaveRequestDto dto) {
        userService.save(dto);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(200)
                .message("Kullanıcı başarı ile kayıt edildi.")
                .data(true)
                .build());
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody UserLoginRequestDto dto) {
        Optional<User> user = userService.login(dto);
        if (user.isEmpty()) {
            throw new AuthException(ErrorType.BAD_REQUEST_USERNAME_OR_PASSWORD_ERROR);
        }
        String token = jwtManager.createToken(user.get().getId());
        return ResponseEntity.ok(ResponseDto.<String>builder().code(200).message("Giriş Başarılı").data(token).build());
    }

    @GetMapping("/search")
    @CrossOrigin("*")
    public ResponseEntity<List<SearchUserResponseDto>> getUserList(String userName) {
        return ResponseEntity.ok(userService.search(userName));
    }

    @GetMapping("/get-profile")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<VwUserProfile>> getProfile(String token) {
        return ResponseEntity.ok(ResponseDto.<VwUserProfile>builder().message("Profil bilgileri").code(200).data(userService.getProfileByToken(token)).build());
    }

    @PostMapping("/edit/profile")
    public ResponseEntity<Boolean> editProfile(@RequestBody User user) {
        userService.editProfile(user);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/get-users-for-follow")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<List<VwUserForFollow>>> getUsersForFollow(String token) {
        return ResponseEntity.ok(ResponseDto.<List<VwUserForFollow>>builder().code(200).message("kullanıcılar başarıyla çekildi").data(userService.getUserNotFollowed(token)).build());
    }


}
