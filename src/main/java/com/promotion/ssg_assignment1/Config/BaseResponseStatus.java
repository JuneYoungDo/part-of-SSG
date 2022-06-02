package com.promotion.ssg_assignment1.Config;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public enum BaseResponseStatus {
    EMPTY_NAME(400,"name은 빈칸일 수 없습니다.",100)
    ;

    private final Timestamp timestamp;
    private final int status;
    private String message;
    private final int code;

    BaseResponseStatus(int status,String message, int code) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
