package com.khuthon.voidteam.dto;


import com.khuthon.voidteam.domain.Job;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String email;
    private String realName;
    private Job job;
    private String password;
    private String university;
    private String nickname;
    private String age;
}
