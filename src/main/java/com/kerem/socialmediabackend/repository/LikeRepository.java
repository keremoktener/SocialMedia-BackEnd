package com.kerem.socialmediabackend.repository;


import com.kerem.socialmediabackend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository  extends JpaRepository<Like,Long> {
}
