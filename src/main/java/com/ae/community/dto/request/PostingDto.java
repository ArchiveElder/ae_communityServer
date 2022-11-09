package com.ae.community.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class PostingDto {
    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 내용")
    private String content;
}
