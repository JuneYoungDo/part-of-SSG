package com.promotion.ssg_assignment1.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyItemToPromotionReq {
    private Long promotionId;
    private Long itemId;
}
