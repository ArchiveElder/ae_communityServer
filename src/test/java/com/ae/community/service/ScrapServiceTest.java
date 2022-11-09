package com.ae.community.service;

import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Posting;
import com.ae.community.domain.Scrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@WebAppConfiguration
@SpringBootTest
@Transactional
public class ScrapServiceTest {
    @Autowired
    CommunityUserService userService;

    @Autowired
    PostingService postingService;

    @Autowired
    ScrapService scrapService;

    @Test
    void 스크랩생성() {
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

        Scrap scrap = Scrap.createScrap(userT.getIdx(), save_post.getIdx());

        // when
        Long id = scrapService.createScrap(scrap);

        // then
        Assertions.assertEquals(id, scrap.getIdx());
    }

    @Test
    void 스크랩삭제() {
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

        Scrap scrap = Scrap.createScrap(userT.getIdx(), save_post.getIdx());
        scrapService.createScrap(scrap);

        // when
        scrapService.deleteScrap(userT.getIdx(), save_post.getIdx());

        // then
        // deleteScrap은 반환값이 void이기 때문에 생략
    }
}
