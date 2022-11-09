package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckMyPostsDto {
    @ApiModelProperty(value = "사용자가 작성한 게시글 개수")
    private Long postCount;
    @ApiModelProperty(value = "사용자가 작성한 게시글 리스트")
    private List<PostsListDto> postsLists;

}
