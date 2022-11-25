package com.ae.community.api;

import com.ae.community.aws.S3Uploader;
import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Images;
import com.ae.community.domain.Posting;

import com.ae.community.dto.response.*;

import com.ae.community.service.ImagesService;
import com.ae.community.service.PostingService;
import com.ae.community.validation.PostValidationController;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/{userIdx}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPost(@PathVariable(value = "userIdx") Long userIdx,
                                           @AuthenticationPrincipal String jwtUserId,
                                           @ApiParam(value = "이미지파일 리스트") @RequestParam(value= "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                           @ApiParam(value = "게시글 제목") @RequestParam(value="title", required =true) String title,
                                           @ApiParam(value = "게시글 내용") @RequestParam(value="content", required = true) String content,
                                           @ApiParam(value = "게시글 내용") @RequestParam(value="boardName") String boardName

    ) throws IOException {

        log.info("POST 31-1 /posting/{userIdx}");
        userValidationController.validateUserByUserIdxAndJwt(userIdx, jwtUserId);
        postValidationController.validationPost(content, title, boardName);

        Posting post = new Posting();
        post = postingService.create(userIdx, content, title, boardName);
        postingService.save(post);
        Long postIdx = post.getIdx();

        if(multipartFileList != null) {
            imagesService.uploadImages(postIdx, multipartFileList);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * [Delete] 31-2 게시글 삭제 API
     */
    @ApiOperation(value = "[POST] 31-2 게시글 삭제 ", notes = "게시글 id로 게시글을 삭제합니다")
    @DeleteMapping ("/{userIdx}/{postIdx}")
    public ResponseEntity<Void> deletePost(@PathVariable (value = "userIdx") Long userIdx,
                                           @PathVariable (value = "postIdx") Long postIdx,
                                           @AuthenticationPrincipal String jwtUserId
    ){
        log.info("Delete 31-2 /posting/{userIdx}/{postIdx}");

        userValidationController.validateUserByUserIdxAndJwt(userIdx, jwtUserId);
        postValidationController.validateDeletePost(postIdx);

        postingService.deletePost(postIdx);

        return ResponseEntity.ok().build();
    }
    /**
     * [Post] 31-3 게시글 수정 API
     * */
    @ApiOperation(value = "[POST] 31-3 게시글 수정 ", notes = "게시글 id로 게시글의 제목과 내용을 수정합니다.")
    @PostMapping(value = "/update/{userIdx}/{postIdx}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(@PathVariable (value = "userIdx") Long userIdx,
                                           @PathVariable (value = "postIdx") Long postIdx,
                                           @AuthenticationPrincipal String jwtUserId,
                                           @ApiParam(value = "이미지파일 리스트") @RequestParam(value= "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                           @ApiParam(value = "게시글 제목") @RequestParam(value="title", required =true) String updateTitle,
                                           @ApiParam(value = "게시글 내용") @RequestParam(value="content", required = true) String updateContent,
                                           @ApiParam(value = "게시글 내용") @RequestParam(value="boardName") String updateBoardName
                                           ) throws IOException {
        log.info("Post 31-3 /posting/update/{userIdx}/{postIdx}");

        userValidationController.validateUserByUserIdxAndJwt(userIdx, jwtUserId);
        postValidationController.validationPost(updateContent, updateTitle, updateBoardName);

        Posting targetPost = postValidationController.validationPostExist(postIdx);
        postingService.update(targetPost, updateTitle, updateContent, updateBoardName);

        Long imgCntInPost = imagesService.getImagesCnt(postIdx);
        if(imgCntInPost > 0) imagesService.deleteByPostIdx(postIdx);

        if(multipartFileList != null) imagesService.uploadImages(postIdx, multipartFileList);

        return ResponseEntity.ok().build();
    }


    /**
     * [Get] 31-4 게시판에 해당하는 게시글 목록 조회 API
     * */
    @ApiOperation(value = "[GET] 31-4 게시판에 해당하는 게시글 목록 조회   ", notes = " 게시판에 해당하는 게시글 목록을 조회 합니다.")
    @GetMapping("/board/{userIdx}/{boardName}")
    public ResponseEntity<PostsLists> allPostsList(@PathVariable (value = "userIdx") Long userIdx,
                                                              @PathVariable (value = "boardName") String boardName,
                                                              @AuthenticationPrincipal String jwtUserId,
                                                              @PageableDefault(size=10) Pageable pageable){

        log.info("Get 31-4 /allposts/{userIdx}");
        CommunityUser user = userValidationController.validateUserByUserIdxAndJwt(userIdx, jwtUserId);

        postValidationController.validateBoardName(boardName);

        List<AllPostsListDto> allPostsList = postingService.getAllPostsInBoard(user, pageable, boardName);
        PostsLists postsLists = new PostsLists();
        postsLists.setPostsList(allPostsList);
        return ResponseEntity.ok().body(postsLists);

    }

    /**

     * [Get] 31-5 게시글 상세 1개 조회 API
     * */
    @ApiOperation(value = "[GET] 31-5 게시글 상세 1개 조회  ", notes = "게시글 id로 게시글의 상세내용을 조회 합니다.")
    @GetMapping("/post/{userIdx}/{postIdx}")
    public ResponseEntity<PostDetailDto> detailPost(@PathVariable (value = "userIdx") Long userIdx,
                                                    @PathVariable (value = "postIdx") Long postIdx,
                                                    @AuthenticationPrincipal String jwtUserId
    ) {
        log.info("Get 31-5 /post/{userIdx}/{postIdx}");

        userValidationController.validateUserByUserIdxAndJwt(userIdx, jwtUserId);
        Posting post = postValidationController.validationPostExist(postIdx);
        List<Images> imageList = imagesService.findByPostIdx(postIdx);

        PostDetailDto postDetailDto = postingService.detailPost(userIdx, postIdx, post, imageList);


        return ResponseEntity.ok().body(postDetailDto);
    }

    /**
     * [Get] 31-6 내가 쓴 게시글 조회 API
     * */
    @ApiOperation(value = "[GET] 내가 쓴 게시글 조회  ", notes = "userIdx로 내가 쓴 게시글들을 조회 합니다.")
    @GetMapping("/mypost/{userIdx}")
    public ResponseEntity<CheckMyPostsDto> checkMyPosts(@PathVariable (value = "userIdx") Long userIdx, @AuthenticationPrincipal String jwtUserId, @PageableDefault(size=10) Pageable pageable) {
        log.info("Post 31-6 /mypost/{userIdx}");
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);

        CheckMyPostsDto checkMyPostsDto = postingService.checkMyPosts(userIdx, pageable);


        return ResponseEntity.ok().body(checkMyPostsDto);
    }

    /**
     * [Get] 31-7 내가 스크랩한 게시글 조회 API
     * */
    @ApiOperation(value = "[GET] 내가 스크랩한 게시글 조회  ", notes = "userIdx로 내가 스크랩한 게시글들을 조회 합니다.")
    @GetMapping("/myscrap/{userIdx}")
    public ResponseEntity<CheckMyScrapsDto> checkMyScraps(@PathVariable (value = "userIdx") Long userIdx, @AuthenticationPrincipal String jwtUserId, @PageableDefault(size=10) Pageable pageable) {
        log.info("Post 31-6 /myscrap/{userIdx}");
        userValidationController.validateUser(userIdx);
        userValidationController.validateUserByJwt(jwtUserId);
        userValidationController.compareUserIdAndJwt(userIdx, jwtUserId);

        CheckMyScrapsDto checkMyScrapsDto = postingService.checkMyScraps(userIdx, pageable);


        return ResponseEntity.ok().body(checkMyScrapsDto);
    }

}
