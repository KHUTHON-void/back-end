package com.khuthon.voidteam.domain;

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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> Likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentFile> Files = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @PrePersist
    public void createdAt(){
        this.createdDate = LocalDateTime.now();
    }

    public void update(String content){
        this.content = content;
    }
    public void increaseLike(){ this.likeCount++;}
    public void decreaseLike(){ this.likeCount--;}

}