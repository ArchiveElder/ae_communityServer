package com.ae.community.service;

import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Posting;
import com.ae.community.domain.Scrap;
import com.ae.community.dto.response.CheckMyPostsDto;
import com.ae.community.dto.response.CheckMyScrapsDto;
import com.ae.community.repository.PostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

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