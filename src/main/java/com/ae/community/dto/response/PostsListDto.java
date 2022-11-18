package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsListDto {
    @ApiModelProperty(value = "게시글 idx")
    private Long postIdx;
    @ApiModelProperty(value = "게시글 작성자 idx")
    private Long userIdx;
    @ApiModelProperty(value = "게시글 작성자 icon")
    private int icon;
    @ApiModelProperty(value = "게시글 작성자 닉네임")
    private String nickname;
    @ApiModelProperty(value = "게시글 종류")
    private String boardName;
    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 내용")
    private String content;
    @ApiModelProperty(value = "게시글 게시날짜")
    private String createdAt;
    @ApiModelProperty(value = "게시글 이미지 유무")
    private int hasImg;
    @ApiModelProperty(value = "게시글 좋아요 개수")
    private int thumbupCount;
    @ApiModelProperty(value = "게시글 댓글 개수")
    private int commentCount;
}
