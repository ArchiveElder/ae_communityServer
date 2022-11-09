package com.ae.community.service;

import com.ae.community.domain.Comment;
import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Posting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@WebAppConfiguration
@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    CommunityUserService userService;

    @Autowired
    PostingService postingService;

    @Autowired
    private CommentService commentService;

    @Test
    void 댓글생성() {
        // given
        CommunityUser user = new CommunityUser();
        String nickname = "dr.김";
        user.setNickname(nickname);
        user.setIdx(13L);
        CommunityUser userT = userService.save(user);

        Posting post = new Posting();
        String content = "안녕하세요";
        String title = "제목";
        Long userIdx = userT.getIdx();
        Posting create_post = postingService.create(userIdx, content, title, "일상");
        Posting save_post = postingService.save(create_post);

        Comment comment = new Comment();
        comment.setUserIdx(userT.getIdx());
        comment.setPostIdx(save_post.getIdx());
        comment.setContent("안녕하세요!");
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // when
        Long id = commentService.createComment(comment);

        // then
        Assertions.assertEquals(id, comment.getIdx());
    }

    @Test
    void 댓글삭제() {
        // given
        CommunityUser user = new CommunityUser();
        String nickname = "dr.김";
        user.setNickname(nickname);
        user.setIdx(13L);
        CommunityUser userT = userService.save(user);

        Posting post = new Posting();
        String content = "안녕하세요";
        String title = "제목";
        Long userIdx = userT.getIdx();
        Posting create_post = postingService.create(userIdx, content, title, "일상");
        Posting save_post = postingService.save(create_post);

        Comment comment = new Comment();
        comment.setUserIdx(userT.getIdx());
        comment.setPostIdx(save_post.getIdx());
        comment.setContent("안녕하세요!");
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(comment);

        // when
        commentService.deleteComment(comment.getIdx());

        // then
        // deleteComment는 반환값이 void이기 생략
    }
}
