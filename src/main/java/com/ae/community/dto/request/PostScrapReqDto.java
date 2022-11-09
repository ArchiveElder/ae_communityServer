package com.ae.community.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostScrapReqDto {
    @ApiModelProperty(value = "게시글 idx")
    private Long postIdx;
}
