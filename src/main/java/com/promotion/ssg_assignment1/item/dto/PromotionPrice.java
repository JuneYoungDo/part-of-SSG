package com.promotion.ssg_assignment1.item.dto;

import com.promotion.ssg_assignment1.promotion.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionPrice {
    private Promotion promotion;
    private double price;
}
