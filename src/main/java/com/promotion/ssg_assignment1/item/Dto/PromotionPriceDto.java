package com.promotion.ssg_assignment1.item.Dto;

import com.promotion.ssg_assignment1.promotion.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionPriceDto {
    private Promotion promotion;
    private double price;
}
