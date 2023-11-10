package com.khuthon.voidteam.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int likeCount;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int commentCount;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> Comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLike> Likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardFile> Files = new ArrayList<>();


    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }

    public void increaseLike(){ this.likeCount++;}
    public void increaseHit(){ this.viewCount++;}

    public void decreaseLike(){ this.likeCount--;}
}
