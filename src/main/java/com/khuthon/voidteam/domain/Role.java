package com.khuthon.voidteam.domain;


import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    UNIV("ROLE_UNIV");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}