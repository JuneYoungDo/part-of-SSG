package com.promotion.ssg_assignment1.Config;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public enum BaseResponseStatus {
    EMPTY_NAME(400,"name은 빈칸일 수 없습니다.",100),
    INVALID_USER_ID(400,"사용할 수 없는 userId 입니다.",101),
    CUSTOM_ERROR(400,"",102),
    INVALID_PERIOD(400,"시작시간 보다 끝나는 시간이 빠를 수 없습니다.",103),
    INVALID_PRICE(400,"가격은 0보다 같거나 작을 수 없습니다.",104),
    INVALID_ITEM_ID(400,"사용할 수 없는 itemId 입니다.",105),
    CAN_NOT_EMPTY_BOTH_OF_DISCOUNT(400,"할인율과 할인 금액이 모두 공백일 수는 없습니다.",106),
    INVALID_DISCOUNT(400,"할인율과 할인 금액은 0보다 작거나 같을 수 없습니다.",107),
    INVALID_PROMOTION_ID(400,"사용할 수 없는 promotionId 입니다.",108)
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
    public void setMessage(String message) {
        this.message = message;
    }
}
