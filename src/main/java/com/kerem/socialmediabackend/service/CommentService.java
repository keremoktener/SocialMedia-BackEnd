package com.kerem.socialmediabackend.service;

import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.AddCommentRequestDto;
import com.kerem.socialmediabackend.dto.request.GetAllCommentByPostIdRequestDto;
import com.kerem.socialmediabackend.dto.response.CommentResponseDto;
import com.kerem.socialmediabackend.entity.Comment;
import com.kerem.socialmediabackend.entity.User;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final JwtManager jwtManager;
    private final UserService userService;
    public void addComment(AddCommentRequestDto dto) {
        Optional<Long> userId =  jwtManager.getAuthId(dto.getToken());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        repository.save(Comment.builder()
                        .comment(dto.getComment())
                        .date(System.currentTimeMillis())
                        .postId(dto.getPostId())
                        .userId(userId.get())
                .build());
    }

    public HashMap<Long, List<CommentResponseDto>> getAllCommentListByPostIds(List<Long> postIds){
        List<Comment> comments = repository.findAllByPostIdIn(postIds);
        List<CommentResponseDto> commentResponseDtos = getCommentResponseDtos(comments,false);
        return commentResponseDtos.stream().collect(Collectors.groupingBy(
           CommentResponseDto::getPostId,
           HashMap::new,
           Collectors.toList()
        ));
    }

    public List<CommentResponseDto> getAllCommentsByPostId(GetAllCommentByPostIdRequestDto dto) {
        if(jwtManager.getAuthId(dto.getToken()).isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        Page<Comment> comments = repository.findAllByPostIdOrderByDateDesc(dto.getPostId(),pageable);
        return getCommentResponseDtos(comments.getContent(),true);
    }

    private List<CommentResponseDto> getCommentResponseDtos(List<Comment> comments, boolean isSize) {
        List<Long> userIds = comments.stream().map(Comment::getUserId).toList();
        Map<Long, User> userMap = userService.findAllByIdsMap(userIds);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        comments.forEach(c->{
           if(commentResponseDtos.stream().filter(cr-> cr.getPostId().equals(c.getPostId())).count()<3 || isSize)
                commentResponseDtos.add(
                        CommentResponseDto.builder()
                                .postId(c.getPostId())
                                .avatar(userMap.get(c.getUserId()).getAvatar())
                                .comment(c.getComment())
                                .commentId(c.getId())
                                .date(c.getDate())
                                .userId(c.getUserId())
                                .userName(userMap.get(c.getUserId()).getUserName())
                                .build()
                );
        });
        return commentResponseDtos;
    }


}
