package com.khuthon.voidteam.domain;

import lombok.Getter;

@Getter
public enum Job {
    HIGHSCHOOL_STUDENT("highscool_student"),
    UNIVERSITY_STUDENT("university_stduent"),
    REPEAT_STUDENT("repeat_student"),
    ETC("etc");

    private  final String job;
    Job(String job){
        this.job = job;
    }


}
