package com.ae.community.api;

import com.ae.community.domain.Thumbup;
import com.ae.community.dto.request.DeleteThumbupReqDto;
import com.ae.community.dto.request.PostThumbupReqDto;
import com.ae.community.dto.response.PostThumbupResDto;
import com.ae.community.dto.response.StringResponseDto;
import com.ae.community.service.ThumbupService;
import com.ae.community.validation.ThumbupValidationController;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Thumbup API", description = "좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/thumbup")
public class ThumbupApiController {
    private final ThumbupService thumbupService;

    private final UserValidationController userValidationController;
    private final ThumbupValidationController thumbupValidationController;

    /**
     * [Post] 41-1 좋아요 등록 API
     * /thumbup/:userIdx
     */
    @ApiOperation(value = "[POST] 41-1 좋아요 등록 ", notes = "userIdx와 postIdx를 넣어 좋아요를 등록합니다")
    @PostMapping("/{userIdx}")
    public ResponseEntity<?> createThumbup(@PathVariable(value = "userIdx", required = false) Long userIdx, @RequestBody PostThumbupReqDto request) {
        //validation 로직
        userValidationController.validateuser(userIdx);
        thumbupValidationController.validatePost(request.getPostIdx());

        Thumbup thumbup = Thumbup.createThumbup(userIdx, request.getPostIdx());
        Long thumbupIdx = thumbupService.createThumbup(thumbup);
        return ResponseEntity.ok().body(new PostThumbupResDto(thumbupIdx));
    }

    /**
     * [Delete] 41-2 좋아요 삭제 API
     * /thumbup/:userIdx
     */
    @ApiOperation(value = "[POST] 41-2 좋아요 삭제 ", notes = "userIdx와 postIdx를 넣어 좋아요를 삭제합니다")
    @DeleteMapping("/{userIdx}")
    public ResponseEntity<?> deleteThumbup(@PathVariable(value = "userIdx", required = false) Long userIdx, @RequestBody DeleteThumbupReqDto request) {
        //validation 로직
        userValidationController.validateuser(userIdx);
        thumbupValidationController.validatePost(request.getPostIdx());
        thumbupValidationController.validateDeleteThumbup(userIdx, request.getPostIdx());

        thumbupService.deleteThumbup(userIdx, request.getPostIdx());
        return ResponseEntity.ok().body(new StringResponseDto("삭제되었습니다."));
    }
}
