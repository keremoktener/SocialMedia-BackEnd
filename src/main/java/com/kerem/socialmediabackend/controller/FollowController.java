package com.kerem.socialmediabackend.controller;

import com.kerem.socialmediabackend.dto.request.CreateFollowSaveDTO;
import com.kerem.socialmediabackend.dto.response.ResponseDto;
import com.kerem.socialmediabackend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/create-follow")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> save(@RequestBody CreateFollowSaveDTO createFollowSaveDTO){
        followService.save(createFollowSaveDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(200)
                .message("Takip edildi.")
                .data(true)
                .build());
    }
}
