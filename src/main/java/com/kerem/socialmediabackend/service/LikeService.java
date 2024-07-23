package com.kerem.socialmediabackend.service;

import com.kerem.socialmediabackend.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository repository;
}
