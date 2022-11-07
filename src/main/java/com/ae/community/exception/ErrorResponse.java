package com.ae.community.exception;

import com.ae.community.dto.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends ApiResponse {
    @Builder
    public ErrorResponse (String code, String message) {
        super(code, message);
    }

}
