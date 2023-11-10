package com.khuthon.voidteam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recruit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int commentCount;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL)
    private List<RecruitComment> RecruitComments = new ArrayList<>();

    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }
    public void increaseHit(){ this.viewCount++;}

}
