package com.ae.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeAndMessage {

    // 유저 관련 1000 ~
    EMPTY_USER("1000", "해당 유저가 없습니다"),
    EMPTY_PATHVARIABLE_USERID("1001", "userIdx pathvariable이 없습니다"),

    // 게시글 관련 1100 ~
    EMPTY_CONTENT("1100", "게시글 내용이 비어있습니다"),
    EMPTY_TITLE("1101", "게시글 제목이 비어있습니다"),
    INVALID_POST_ID("1102", "유효하지 않은 postIdx 입니다."),

    // 이미지 관련 1200 ~
    NO_MATCHING_IMAGES("1200", "imagesIdx에 해당하는 것이 없습니다."),
    EMPTY_MULTIPARTFILE("1201", "이미지 파일이 비었습니다"),

    // 댓글 관련 1300 ~
    COMMENT_NO_CONTENT("1300", "댓글 내용이 비어있습니다."),
    COMMENT_LONG_CONTENT("1301", "댓글 내용이 너무 깁니다."),
    INVALID_COMMENT("1302", "유효하지 않은 comment 입니다."),

    // 좋아요 관련 1400 ~
    INVALID_THUMBUP("1400", "유효하지 않은 thumbup 입니다."),

    // S3 Util
    FILE_CONVERT_ERROR("1200", "파일 변환에 실패했습니다."),
    FILE_EXTENSION_ERROR("1201", "파일 확장자 인식에 실패했습니다.");



    private final String code;
    private final String message;

}
