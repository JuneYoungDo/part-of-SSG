package com.promotion.ssg_assignment1.promotion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> getByPromotionId(Long promotionId);
}
