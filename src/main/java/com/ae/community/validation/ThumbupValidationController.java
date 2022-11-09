package com.ae.community.validation;

import com.ae.community.domain.Posting;
import com.ae.community.domain.Thumbup;
import com.ae.community.exception.chaebbiException;
import com.ae.community.service.PostingService;
import com.ae.community.service.ThumbupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.ae.community.exception.CodeAndMessage.INVALID_POST_ID;
import static com.ae.community.exception.CodeAndMessage.INVALID_THUMBUP;

@Controller
@RequiredArgsConstructor
public class ThumbupValidationController {
    private final PostingService postingService;
    private final ThumbupService thumbupService;
    public void validatePost(Long postIdx) {
        Optional<Posting> post = postingService.findById(postIdx);
        if(post == Optional.<Posting>empty()) throw new chaebbiException(INVALID_POST_ID);
    }

    public void validateDeleteThumbup(Long userIdx, Long postIdx) {
        Optional<Thumbup> thumbup = thumbupService.findByUserIdxAndPostIdx(userIdx, postIdx);
        if(thumbup == Optional.<Thumbup>empty()) {
            throw new chaebbiException(INVALID_THUMBUP);
        }
    }
}
