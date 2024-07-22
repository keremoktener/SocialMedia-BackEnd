package com.kerem.socialmediabackend.service;

import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.CreateFollowSaveDTO;
import com.kerem.socialmediabackend.entity.Follow;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.repository.FollowRepository;
import com.kerem.socialmediabackend.utility.FollowState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final JwtManager jwtManager;


    public List<Long> findFollowedUserIds(Long userId){
        return  followRepository.findFollowedUserIdsByUserId(userId);
    }

    public void save(CreateFollowSaveDTO createFollowSaveDTO) {
        Long userId = jwtManager.getAuthId(createFollowSaveDTO.getToken()).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        followRepository.save(Follow.builder()
                        .userId(userId)
                        .followId(createFollowSaveDTO.getFollowId())
                        .state(FollowState.FOLLOWED)
                .build());

    }
}
