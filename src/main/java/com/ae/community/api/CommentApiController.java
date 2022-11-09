package com.ae.community.api;

import com.ae.community.domain.Comment;
import com.ae.community.dto.request.DeleteCommentReqDto;
import com.ae.community.dto.request.PostCommentReqDto;
import com.ae.community.dto.response.PostCommentResDto;
import com.ae.community.dto.response.StringResponseDto;
import com.ae.community.service.CommentService;
import com.ae.community.validation.CommentValidationController;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Comment API", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentApiController {
    private final CommentService commentService;
    private final UserValidationController userValidationController;
    private final CommentValidationController commentValidationController;

    /**
     * [Post] 40-1 댓글 작성 API
     * /comment/:userIdx
     */
    @ApiOperation(value = "[POST] 40-1 댓글 작성 ", notes = "postIdx, 댓글 내용을 넣어 댓글을 등록합니다")
    @PostMapping("/{userIdx}")
    public ResponseEntity<?> createComment(@PathVariable(value = "userIdx", required = false) Long userIdx, @AuthenticationPrincipal String jwtUserId,  @RequestBody PostCommentReqDto request) {
        //validation 로직
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);
        commentValidationController.validateComment(request);

        Comment comment = Comment.createComment(userIdx, request.getPostIdx(), request.getContent());
        Long commentIdx = commentService.createComment(comment);
        return ResponseEntity.ok().body(new PostCommentResDto(commentIdx));
    }

    /**
     * [Delete] 40-2 댓글 삭제 API
     * /comment/:userIdx
     */
    @ApiOperation(value = "[DELETE] 40-2 댓글 삭제 ", notes = "commentIdx를 넣어 댓글을 삭제합니다")
    @DeleteMapping("/{userIdx}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "userIdx", required = false) Long userIdx, @AuthenticationPrincipal String jwtUserId, @RequestBody DeleteCommentReqDto request) {
        //validation 로직
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);
        commentValidationController.validateDeleteComment(userIdx, request.getCommentIdx());

        commentService.deleteComment(request.getCommentIdx());
        return ResponseEntity.ok().body(new StringResponseDto("삭제되었습니다."));
    }
}
