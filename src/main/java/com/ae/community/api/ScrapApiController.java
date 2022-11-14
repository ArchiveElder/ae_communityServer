package com.ae.community.api;

import com.ae.community.domain.Scrap;
import com.ae.community.dto.request.*;
import com.ae.community.dto.response.PostScrapResDto;
import com.ae.community.dto.response.StringResponseDto;
import com.ae.community.service.ScrapService;
import com.ae.community.validation.ScrapValidationController;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "Scrap API", description = "스크랩 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapApiController {
    private final ScrapService scrapService;
    private final UserValidationController userValidationController;
    private final ScrapValidationController scrapValidationController;

    /**
     * [Post] 42-1 스크랩 등록 API
     * /scrap/:userIdx
     */
    @ApiOperation(value = "[POST] 42-1 스크랩 등록 ", notes = "userIdx와 postIdx를 넣어 스크랩을 등록합니다")
    @PostMapping("/{userIdx}")
    public ResponseEntity<?> createScrap(@PathVariable(value = "userIdx", required = false) Long userIdx, @AuthenticationPrincipal String jwtUserId, @RequestBody PostScrapReqDto request) {
        //validation 로직
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);
        scrapValidationController.validatePost(request.getPostIdx());

        Optional<Scrap> check = scrapService.findByUserIdxAndPostIdx(userIdx, request.getPostIdx());
        if(check != Optional.<Scrap>empty()) {
            scrapService.deleteScrap(userIdx, request.getPostIdx());
        }
        Scrap scrap = Scrap.createScrap(userIdx, request.getPostIdx());
        Long scrapIdx = scrapService.createScrap(scrap);
        return ResponseEntity.ok().body(new PostScrapResDto(scrapIdx));
    }

    /**
     * [Delete] 42-2 스크랩 삭제 API
     * /scrap/:userIdx
     */
    @ApiOperation(value = "[POST] 42-2 스크랩 삭제 ", notes = "userIdx와 postIdx를 넣어 스크랩을 삭제합니다")
    @DeleteMapping("/{userIdx}")
    public ResponseEntity<?> deleteScrap(@PathVariable(value = "userIdx", required = false) Long userIdx, @AuthenticationPrincipal String jwtUserId, @RequestBody DeleteScrapReqDto request) {
        //validation 로직
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);
        scrapValidationController.validatePost(request.getPostIdx());
        scrapValidationController.validateDeleteScrap(userIdx, request.getPostIdx());

        scrapService.deleteScrap(userIdx, request.getPostIdx());
        return ResponseEntity.ok().body(new StringResponseDto("삭제되었습니다."));
    }
}
