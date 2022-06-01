package com.promotion.ssg_assignment1.promotion;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
}
