package com.promotion.ssg_assignment1.item.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetItemPromotionRes {
    private Long promotionId;
    private String name;
    private double originPrice;
    private double discountPrice;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
}
