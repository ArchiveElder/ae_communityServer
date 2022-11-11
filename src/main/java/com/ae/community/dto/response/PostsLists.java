package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostsLists {
    @ApiModelProperty(value = "페이징 처리된 게시글 리스트 ")
    List<AllPostsListDto> postsList;
}
