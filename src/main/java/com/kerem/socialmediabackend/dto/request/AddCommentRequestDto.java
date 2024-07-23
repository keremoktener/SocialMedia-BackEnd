package com.kerem.socialmediabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCommentRequestDto {
    String token;
    Long postId;
    String comment;
}
