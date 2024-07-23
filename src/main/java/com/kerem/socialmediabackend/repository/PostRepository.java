package com.kerem.socialmediabackend.repository;


import com.kerem.socialmediabackend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
