package com.ae.community.service;

import com.ae.community.domain.*;
import com.ae.community.dto.response.PostDetailDto;
import com.ae.community.repository.PostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@WebAppConfiguration
@SpringBootTest
@Transactional

class PostingServiceTest {
    @Autowired
    CommunityUserService userService;

    @Autowired
    PostingService postingService;

    @Autowired
    PostingRepository postingRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ThumbupService thumbupService;
    @Autowired
    private ImagesService imagesService;

    @Test
    void 포스트저장() {
        CommunityUser user = new CommunityUser();
        String nickname = "dr.김";
        user.setNickname(nickname);
        user.setIdx(13L);
        CommunityUser userT = userService.save(user);

        Posting post = new Posting();
        String content = "안녕하세요 오늘은 테스트입니다";
        String title = "안녕하세요 테스트 제목임";
        Long userIdx = userT.getIdx();
        Posting create_post = postingService.create(userIdx, content, title, "일상");
        Posting save_post = postingService.save(create_post);

    }
    @Test
    void 게시글_한개_조회() {
        // user Kim 과 Choi 생성
        CommunityUser user1 = new CommunityUser();
        user1.setNickname("dr.김");
        user1.setIdx(333L);
        user1.setUserIdx(13L);
        CommunityUser userK = userService.save(user1);

        CommunityUser user2 = new CommunityUser();
        user2.setNickname("dr.최");
        user2.setIdx(444L);
        user2.setUserIdx(14L);
        CommunityUser userC = userService.save(user2);

        //Kim이 게시글 생성
        Posting createPost = postingService.create(userK.getUserIdx(), "new post", "new post title by Kim", "일상");
        Posting savedPost =postingService.save(createPost);    // 포스트 저장

        //Choi가 댓글 달고 따봉함
        Comment comment = new Comment();
        comment.setUserIdx(userC.getUserIdx());
        comment.setPostIdx(savedPost.getIdx());
        comment.setContent("안녕하세요! 김선생님 최입니다.");
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(comment);

        Thumbup thumbup = Thumbup.createThumbup(userC.getIdx(), savedPost.getIdx());
        thumbupService.createThumbup(thumbup);

        List<Images> imageList = imagesService.findByPostIdx(savedPost.getIdx());
        PostDetailDto postDetailDto = postingService.detailPost(userC.getUserIdx(), savedPost.getIdx(), savedPost, imageList);

        // 게시글의 작성자가 Kim이 맞는가
        assertEquals(userK.getNickname(), postDetailDto.getNickname());
        // 게시글 댓글의 닉네임이 Choi가 맞는가
        assertEquals(userC.getNickname(), postDetailDto.getCommentsLists().get(0).getNickname());
        // 따봉의 수가 1개가 맞는가
        assertEquals(1, postDetailDto.getThumbupCount());
        // 댓글의 수가 1개가 맞는가
        assertEquals(1, postDetailDto.getCommentCount());


    }
}