package com.kerem.socialmediabackend.repository;


import com.kerem.socialmediabackend.entity.Follow;
import com.kerem.socialmediabackend.utility.FollowState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository  extends JpaRepository<Follow,Long> {
    Optional<Follow> findOptionalByUserIdAndFollowId(Long userId, Long followId);

    List<Follow> findAllByUserId(Long userId);

    List<Follow> findAllByUserIdAndStateIn(Long userId, List<FollowState> takipEdiyor);
}
