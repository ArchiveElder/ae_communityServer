package com.ae.community.validation;

import com.ae.community.domain.Posting;
import com.ae.community.exception.chaebbiException;
import com.ae.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.ae.community.exception.CodeAndMessage.*;


@Controller
@RequiredArgsConstructor
public class PostValidationController {
    private final PostingService postingService;

    public void validationPost(String content, String title, String boardName){
        if(content == null || content.equals("")) throw new chaebbiException(EMPTY_CONTENT);
        if(title == null || title.equals("")) throw new chaebbiException(EMPTY_TITLE);
        if(boardName == null || boardName.equals("")) throw new chaebbiException(EMPTY_GROUPNAME);
    }

    public void validateDeletePost(Long postIdx) {
        Optional<Posting> post = postingService.findById(postIdx);
        if(post == Optional.<Posting>empty()) throw new chaebbiException(INVALID_POST_ID);
    }

    public Posting validationPostExist(Long postIdx) {
        Optional<Posting> post = postingService.findById(postIdx);
        if(post == Optional.<Posting>empty()) throw new chaebbiException(INVALID_POST_ID);
        else return post.get();

    }

    public void validateBoardName(String boardName) {
        if(boardName == null || boardName.equals("")) throw new chaebbiException(EMPTY_PATHVARIABLE_GROUPNAME);
    }
}
