package com.ae.community.validation;

import com.ae.community.domain.Comment;
import com.ae.community.domain.Posting;
import com.ae.community.dto.request.PostCommentReqDto;
import com.ae.community.exception.chaebbiException;
import com.ae.community.service.CommentService;
import com.ae.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.ae.community.exception.CodeAndMessage.*;

@Controller
@RequiredArgsConstructor
public class CommentValidationController {
    private final PostingService postingService;
    private final CommentService commentService;

    public void validateComment(PostCommentReqDto request) {
        Optional<Posting> post = postingService.findById(request.getPostIdx());
        if(post == Optional.<Posting>empty()) throw new chaebbiException(INVALID_POST_ID);

        if(request.getContent().isEmpty() || request.getContent().equals("")) {
            throw new chaebbiException(COMMENT_NO_CONTENT);
        }

        if(request.getContent().length() > 200) {
            throw new chaebbiException(COMMENT_LONG_CONTENT);
        }
    }

    public void validateDeleteComment(Long userIdx, Long commentIdx) {
        Optional<Comment> comment = commentService.findByUserIdxAndIdx(userIdx, commentIdx);
        if(comment == Optional.<Comment>empty()) {
            throw new chaebbiException(INVALID_COMMENT);
        }
    }
}
