package com.khuthon.voidteam.domain;

import lombok.Getter;

@Getter
public enum Grade {
    IRON("iron"),
    BRONZE("bronze"),
    SILVER("silver"),
    GOLD("gold");

    private final String grade;

    Grade(String grade) {
        this.grade = grade;
    }
}
