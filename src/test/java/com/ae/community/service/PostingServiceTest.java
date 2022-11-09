package com.ae.community.service;

import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Posting;
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
}