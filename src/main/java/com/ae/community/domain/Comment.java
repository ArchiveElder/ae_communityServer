package com.ae.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "post_idx")
    private Long postIdx;

    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public static Comment createComment(Long userIdx, Long postIdx, String content) {
        Comment comment = new Comment();
        comment.setUserIdx(userIdx);
        comment.setPostIdx(postIdx);
        comment.setContent(content);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return comment;
    }
}
