package com.kerem.socialmediabackend.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VwSearchUser {
    Long id;
    String userName;
    String name;
    String avatar;
}
