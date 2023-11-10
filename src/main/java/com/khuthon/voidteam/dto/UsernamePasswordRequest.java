package com.khuthon.voidteam.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsernamePasswordRequest {
    private String email;
    private String password;
}
