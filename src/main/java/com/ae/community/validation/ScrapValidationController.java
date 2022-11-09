package com.ae.community.validation;

import com.ae.community.domain.Posting;
import com.ae.community.domain.Scrap;
import com.ae.community.exception.chaebbiException;
import com.ae.community.service.PostingService;
import com.ae.community.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.ae.community.exception.CodeAndMessage.*;

@Controller
@RequiredArgsConstructor
public class ScrapValidationController {
    private final PostingService postingService;
    private final ScrapService scrapService;

    public void validatePost(Long postIdx) {
        Optional<Posting> post = postingService.findById(postIdx);
        if(post == Optional.<Posting>empty()) throw new chaebbiException(INVALID_POST_ID);
    }

    public void validateDeleteScrap(Long userIdx, Long postIdx) {
        Optional<Scrap> scrap = scrapService.findByUserIdxAndPostIdx(userIdx, postIdx);
        if(scrap == Optional.<Scrap>empty()) {
            throw new chaebbiException(INVALID_SCRAP);
        }
    }
}
