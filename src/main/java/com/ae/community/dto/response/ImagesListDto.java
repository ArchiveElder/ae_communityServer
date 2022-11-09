package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImagesListDto {
    @ApiModelProperty(value = "조회 요청한 게시글의 이미지 url")
    private String imageUrl;
    @ApiModelProperty(value = "조회 요청한 게시글의 이미지 순서")
    private int imgRank;

}
