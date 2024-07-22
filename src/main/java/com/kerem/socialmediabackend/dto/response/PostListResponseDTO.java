package com.kerem.socialmediabackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostListResponseDTO {
    private Long id;
    private Long userID;
    private String userName;
    private String avatar;
    private String photo;
    private String comment;
    private Long likeCount;
    private Long commentCount;
    private Long sharedDate;
    private List<CommentListResponseDTO> commentList;
}
