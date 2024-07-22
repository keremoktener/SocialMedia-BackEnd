package com.kerem.socialmediabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateFollowSaveDTO {
    private String token; //Takip etmek isteyen kişi
    private Long followId; //takip edilecek kişi
}
