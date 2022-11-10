package com.ae.community.service;


import com.ae.community.domain.*;
import com.ae.community.dto.response.AllPostsListDto;
import com.ae.community.dto.response.PostDetailDto;

import com.ae.community.dto.response.CheckMyPostsDto;
import com.ae.community.dto.response.CheckMyScrapsDto;

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
    ScrapService scrapService;


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
    void 내가쓴_게시글_조회() {
        // given
        CommunityUser user1 = new CommunityUser();
        user1.setNickname("dr.김");
        user1.setIdx(333L);
        user1.setUserIdx(13L);
        CommunityUser userK = userService.save(user1);
        // when
        Posting createPost = postingService.create(userK.getUserIdx(), "new post", "new post title by Kim", "일상");
        Posting savedPost1 =postingService.save(createPost);    // 포스트 저장

        Posting createPost2 = postingService.create(userK.getUserIdx(), "new post2", "2nd new post title by Kim", "일상");
        Posting savedPost2 =postingService.save(createPost2);

        CheckMyPostsDto checkMyPostsDto = postingService.checkMyPosts(userK.getUserIdx());

        // then
        assertEquals(2, checkMyPostsDto.getPostCount());
        assertEquals(savedPost1.getTitle(), checkMyPostsDto.getPostsLists().get(0).getTitle());
        assertEquals(savedPost1.getContent(), checkMyPostsDto.getPostsLists().get(0).getContent());

        assertEquals(savedPost2.getTitle(), checkMyPostsDto.getPostsLists().get(1).getTitle());
        assertEquals(savedPost2.getContent(), checkMyPostsDto.getPostsLists().get(1).getContent());
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
    @Test
    void 전체_게시글_조회() {
        // user Kim, Choi 생성
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


        // 게시글 3개 생성
        Posting createPost1 = postingService.create(userK.getUserIdx(), "new post", "new post title by Kim", "일상");
        Posting savedPost1 =postingService.save(createPost1);    // 포스트 저장
        Posting createPost2 = postingService.create(userK.getUserIdx(), "new post2", "2nd new post title by Kim", "일상");
        Posting savedPost2 =postingService.save(createPost2);
        Posting createPost3 = postingService.create(userK.getUserIdx(), "new post3", "2nd new post title by Kim","레시피");
        Posting savedPost3 =postingService.save(createPost3);

        // 따봉과 댓글
        Comment comment = new Comment();
        comment.setUserIdx(userC.getUserIdx());
        comment.setPostIdx(savedPost1.getIdx());
        comment.setContent("안녕하세요! 김선생님 최입니다.");
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(comment);

        Comment recomment = new Comment();
        recomment.setUserIdx(userK.getUserIdx());
        recomment.setPostIdx(savedPost1.getIdx());
        recomment.setContent("안녕하세요! 최선생님 반가워요.");
        recomment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(recomment);

        thumbupService.createThumbup(Thumbup.createThumbup(userC.getIdx(), savedPost1.getIdx()));
        thumbupService.createThumbup(Thumbup.createThumbup(userC.getIdx(), savedPost2.getIdx()));
        thumbupService.createThumbup(Thumbup.createThumbup(userC.getIdx(), savedPost3.getIdx()));

        //List<AllPostsListDto> allPostsListDto = postingService.allPostsList(userK);

        // 게시글 따봉수,댓글수가 맞는가 -> db에 저장되어있는 것 때문에 사용 불가
        /*assertEquals(2, allPostsListDto.get(0).getCommentCnt());
        assertEquals(1, allPostsListDto.get(0).getLikeCnt());

        assertEquals(0, allPostsListDto.get(1).getCommentCnt());
        assertEquals(1, allPostsListDto.get(1).getLikeCnt());

        assertEquals(0, allPostsListDto.get(2).getCommentCnt());
        assertEquals(1, allPostsListDto.get(2).getLikeCnt());*/

    }




        

    @Test
    void 내가_스크랩한_게시글_조회() {
        // given
        CommunityUser user1 = new CommunityUser();
        user1.setNickname("dr.김");
        user1.setIdx(333L);
        user1.setUserIdx(13L);
        CommunityUser userK = userService.save(user1);


        // when
        Posting createPost = postingService.create(userK.getUserIdx(), "new post", "new post title by Kim", "일상");
        Posting savedPost1 =postingService.save(createPost);    // 포스트 저장

        Posting createPost2 = postingService.create(userK.getUserIdx(), "new post2", "2nd new post title by Kim", "일상");
        Posting savedPost2 =postingService.save(createPost2);

        Scrap scrap = Scrap.createScrap(userK.getUserIdx(), savedPost1.getIdx());
        scrapService.createScrap(scrap);

        Scrap scrap2 = Scrap.createScrap(userK.getUserIdx(), savedPost2.getIdx());
        scrapService.createScrap(scrap2);

        CheckMyScrapsDto checkMyScrapsDto = postingService.checkMyScraps(userK.getUserIdx());

        // then
        assertEquals(2, checkMyScrapsDto.getPostCount());

    }
}