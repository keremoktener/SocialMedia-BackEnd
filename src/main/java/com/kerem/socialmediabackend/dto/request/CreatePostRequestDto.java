package com.kerem.socialmediabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePostRequestDto {
    String comment;
    String token;
    String url;

}
