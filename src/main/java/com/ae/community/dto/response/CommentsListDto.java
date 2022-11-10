package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsListDto {
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 id")
    private Long commentIdx;
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 작성자의 userId")
    private Long userIdx;
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 작성자 닉네임")
    private String nickname;
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 작성자 아이콘")
    private int icon;
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 게시일자")
    private String date;
    @ApiModelProperty(value = "조회 요청한 게시글의 댓글 내용")
    private String content;


}