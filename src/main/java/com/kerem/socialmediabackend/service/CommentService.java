package com.kerem.socialmediabackend.service;

import com.kerem.socialmediabackend.config.JwtManager;
import com.kerem.socialmediabackend.dto.request.CreateCommentSaveDTO;
import com.kerem.socialmediabackend.dto.response.CommentListResponseDTO;
import com.kerem.socialmediabackend.entity.Comment;
import com.kerem.socialmediabackend.exception.AuthException;
import com.kerem.socialmediabackend.exception.ErrorType;
import com.kerem.socialmediabackend.repository.CommentRepository;
import com.kerem.socialmediabackend.view.VwUserAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    private final JwtManager jwtManager;

    public void saveComment(CreateCommentSaveDTO createCommentSaveDTO) {
        Long userId = jwtManager.getAuthId(createCommentSaveDTO.getToken()).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        commentRepository.save(Comment.builder()
                .comment(createCommentSaveDTO.getComment())
                .userId(userId)
                .postId(createCommentSaveDTO.getPostId())
                .date(System.currentTimeMillis())
                .build()
        );

    }

    public List<CommentListResponseDTO> getCommentsByPostID(Long id) {
        List<Comment> commentList = commentRepository.findAllByPostId(id);
        List<CommentListResponseDTO> result = new ArrayList<>();
        commentList.forEach(comment -> {
            VwUserAvatar userAvatarAndUserName = userService.getUserAvatarAndUserName(comment.getUserId());
            result.add(CommentListResponseDTO.builder()
                            .comment(comment.getComment())
                            .date(comment.getDate())
                            .postId(comment.getPostId())
                            .userId(comment.getUserId())
                            .userAvatar(userAvatarAndUserName.getAvatar())
                            .userName(userAvatarAndUserName.getUserName())
                    .build());
        });

        return result;
    }

//    private HashMap<Long, List<CommentListResponseDTO>> getAllCommentsByPostIds(List<Long> postIds) {
//        List<Comment> commentList = commentRepository.findAllByPostIdIn(postIds);
//        List<Long> userIds= commentList.stream().map(Comment::getUserId).toList();
//        Map<Long, User> userMap = userService.findAllbyIdMap(userIds);
//        List<CommentListResponseDTO> result = new ArrayList<>();
//        commentList.forEach(comment -> {
//            User user = userMap.get(comment.getUserId());
//            result.add(CommentListResponseDTO.builder()
//                            .comment(comment.getComment())
//                            .date(comment.getDate())
//                            .postId(comment.getPostId())
//                            .userId(comment.getUserId())
//                            .userAvatar(user.getAvatar())
//                            .userName(user.getName())
//                    .build());
//        });
//        return result.stream().collect(Collectors.groupingBy(
//                CommentListResponseDTO::getPostId,
//                HashMap::new,
//                Collectors.toList()
//        ));
//
//
//    }
}
