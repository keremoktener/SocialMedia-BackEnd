package com.kerem.socialmediabackend.controller;


import com.kerem.socialmediabackend.dto.request.AddFollowRequestDto;
import com.kerem.socialmediabackend.dto.response.ResponseDto;
import com.kerem.socialmediabackend.service.FollowService;
import com.kerem.socialmediabackend.service.UserService;
import com.kerem.socialmediabackend.views.VwSearchUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@CrossOrigin(origins = "*", methods ={RequestMethod.POST,RequestMethod.GET})
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    @PostMapping("/add-follow")
    public ResponseEntity<ResponseDto<Boolean>> addFollow(@RequestBody AddFollowRequestDto dto){
        followService.addFollow(dto);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                        .data(true)
                        .message("Ok")
                        .code(200)
                .build());
    }

    @PostMapping("/un-follow")
    public ResponseEntity<ResponseDto<Boolean>> unFollow(@RequestBody AddFollowRequestDto dto){
        followService.unFollow(dto);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .data(true)
                .message("Ok")
                .code(200)
                .build());
    }

    @GetMapping("/get-all-following")
    public ResponseEntity<ResponseDto<List<VwSearchUser>>> getAllFollowing(String token){
        List<VwSearchUser> result = userService.getAllFollowList(followService.getAllFollowing(token));
        return ResponseEntity.ok(
                ResponseDto.<List<VwSearchUser>>builder()
                        .code(200)
                        .message("ok")
                        .data(result)
                        .build()
        );
    }

}
