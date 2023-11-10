package com.khuthon.voidteam.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum SortType {
    LIKE("LIKE"),
    VIEW("VIEW"),
    CHRONOLOGICAL("CHRONOLOGICAL");


    private String sortType;
}
