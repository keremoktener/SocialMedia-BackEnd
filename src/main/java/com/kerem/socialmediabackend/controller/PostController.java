package com.kerem.socialmediabackend.controller;

import com.kerem.socialmediabackend.dto.request.CreatePostRequestDTO;
import com.kerem.socialmediabackend.dto.response.PostListResponseDTO;
import com.kerem.socialmediabackend.dto.response.ResponseDto;
import com.kerem.socialmediabackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;


    @PostMapping("/create-post")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> createPost(@RequestBody CreatePostRequestDTO createPostRequestDTO){
        postService.createPost(createPostRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().data(true).code(200).message("Post Paylaşıldı!").build());
    }


    @GetMapping("/get-post-list")
    @CrossOrigin("*")
    public  ResponseEntity<ResponseDto<List<PostListResponseDTO>>> getPostList(String token){
        return ResponseEntity.ok(ResponseDto.<List<PostListResponseDTO>>builder().message("Post Listesi getirildi").code(200).data(postService.getPostList(token)).build());
    }







}
