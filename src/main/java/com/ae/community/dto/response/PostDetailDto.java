package com.ae.community.dto.response;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailDto {
    @ApiModelProperty(value = "조회 요청한 게시글 id")
    private Long postIdx;
    @ApiModelProperty(value = "조회 요청한 게시글 게시판 종류")
    private String boardName;
    @ApiModelProperty(value = "조회 요청한 게시글 제목")
    private String title;
    @ApiModelProperty(value = "조회 요청한 게시글 내용")
    private String content;
    @ApiModelProperty(value = "조회 요청한 게시글 작성자 아이콘")
    private int icon;
    @ApiModelProperty(value = "조회 요청한 게시글 작성자의 userId")
    private Long userIdx;
    @ApiModelProperty(value = "조회 요청한 게시글 작성자 닉네임 ")
    private String nickname;
    @ApiModelProperty(value = "조회 요청한 게시글 게시일자 yyyy.MM.dd HH:mm")
    private String createdAt;
    @ApiModelProperty(value = "조회 요청한 게시글 이미지 개수")
    private int imagesCount;
    @ApiModelProperty(value = "조회 요청한 게시글 이미지 리스트")
    private List<ImagesListDto> imagesLists;
    @ApiModelProperty(value = "조회 요청한 게시글 댓글 수")
    private Long thumbupCount;
    @ApiModelProperty(value = "조회 요청한 게시글 따봉 수")
    private Long commentCount;
    @ApiModelProperty(value = "조회 요청한 게시글 사용자가 좋아요한 여부")
    private boolean isLiked;
    @ApiModelProperty(value = "조회 요청한 게시글 사용자가 스크랩 여부")
    private boolean isScraped;

    @ApiModelProperty(value = "조회 요청한 게시글 댓글 리스트")
    private List<CommentsListDto> commentsLists;
}
