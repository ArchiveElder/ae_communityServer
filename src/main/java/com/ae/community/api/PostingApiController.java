package com.ae.community.api;

import com.ae.community.aws.S3Uploader;
import com.ae.community.domain.Images;
import com.ae.community.domain.Posting;
import com.ae.community.dto.request.PostingDto;
import com.ae.community.dto.response.CheckMyPostsDto;
import com.ae.community.dto.response.CheckMyScrapsDto;
import com.ae.community.service.ImagesService;
import com.ae.community.service.PostingService;
import com.ae.community.validation.PostValidationController;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Api(tags = "Posting API", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posting")
public class PostingApiController {

    private final PostingService postingService;
    private final ImagesService imagesService;
    private final S3Uploader s3Uploader;
    private final UserValidationController userValidationController;
    private final PostValidationController postValidationController;
    /**
     * [Post] 31-1 게시글 작성 API
     */
    @ApiOperation(value = "[POST] 31-1 게시글 작성 ", notes = "제목, 글내용, 이미지들을 넣어 게시글을 등록합니다")
    @PostMapping("/{userIdx}")
    public ResponseEntity<Void> uploadPost(@PathVariable(value = "userIdx") Long userIdx, @AuthenticationPrincipal String jwtUserId,
                                           @ApiParam(value = "이미지파일 리스트") @RequestPart(value= "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                           @ApiParam(value = "게시글 제목과 내용 dto") @RequestPart(value="posting") PostingDto postingDto ) throws IOException {

        log.info("POST 31-1 /posting/{userIdx}");

        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);
        postValidationController.validationPost(postingDto.getContent(), postingDto.getTitle());

        Posting post = new Posting();
        post = postingService.create(userIdx, postingDto.getContent(), postingDto.getTitle(), postingDto.getGroupName());
        postingService.save(post);
        Long postIdx = post.getIdx();

        int img_rank = 1;

        for(int i = 0; i < multipartFileList.size(); i++) {
            MultipartFile multipartFile = multipartFileList.get(i);
            String img_url = "empty";
            if(multipartFile != null) {
                if(!multipartFile.isEmpty()) {
                    img_url = s3Uploader.upload(multipartFile, "static");
                    Images images = imagesService.create(postIdx, img_url, img_rank);
                    imagesService.save(images);
                    img_rank++;
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    /**
     * [Get] 31-6 내가 쓴 게시글 조회 API
     * */
    @ApiOperation(value = "[GET] 내가 쓴 게시글 조회  ", notes = "userIdx로 내가 쓴 게시글들을 조회 합니다.")
    @GetMapping("/mypost/{userIdx}")
    public ResponseEntity<CheckMyPostsDto> checkMyPosts(@PathVariable (value = "userIdx") Long userIdx, @AuthenticationPrincipal String jwtUserId) {
        log.info("Post 31-6 /mypost/{userIdx}");
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);

        CheckMyPostsDto checkMyPostsDto = postingService.checkMyPosts(userIdx);


        return ResponseEntity.ok().body(checkMyPostsDto);
    }

    /**
     * [Get] 31-7 내가 스크랩한 게시글 조회 API
     * */
    @ApiOperation(value = "[GET] 내가 스크랩한 게시글 조회  ", notes = "userIdx로 내가 스크랩한 게시글들을 조회 합니다.")
    @GetMapping("/myscrap/{userIdx}")
    public ResponseEntity<CheckMyScrapsDto> checkMyScraps(@PathVariable (value = "userIdx") Long userIdx, @AuthenticationPrincipal String jwtUserId) {
        log.info("Post 31-6 /myscrap/{userIdx}");
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);

        CheckMyScrapsDto checkMyScrapsDto = postingService.checkMyScraps(userIdx);


        return ResponseEntity.ok().body(checkMyScrapsDto);
    }
}
