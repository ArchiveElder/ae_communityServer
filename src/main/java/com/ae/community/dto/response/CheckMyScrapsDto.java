package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckMyScrapsDto {
    @ApiModelProperty(value = "사용자가 스크랩한 게시글 개수")
    private Long postCount;
    @ApiModelProperty(value = "사용자가 스크랩한 게시글 리스트")
    private List<PostsListDto> postsLists;
}
