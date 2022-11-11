package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class AllPostsListDto {
    @ApiModelProperty(value = "게시글 id")
    private Long postIdx;
    @ApiModelProperty(value = "게시글 종류")
    private String boardName;
    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 작성자 userId")
    private Long userIdx;
    @ApiModelProperty(value = "게시글 작성자 아이콘")
    private int icon;
    @ApiModelProperty(value = "게시글 작성자 닉네임 ")
    private String nickname;
    @ApiModelProperty(value = "게시글 게시일자 yyyy.MM.dd HH:mm")
    private String createdAt;
    @ApiModelProperty(value = "게시글 이미지 유무")
    private int hasImg;
    @ApiModelProperty(value = "게시글 따봉 수")
    private Long LikeCnt;
    @ApiModelProperty(value = "게시글  댓글 수")
    private Long commentCnt;
    @ApiModelProperty(value = "게시글 따봉 여부")
    private int isScraped;

}
