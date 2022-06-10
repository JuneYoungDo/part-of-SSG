package com.promotion.ssg_assignment1.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @Query(value = "select p from Promotion p where p.promotionId = :promotionId and p.deleted = false")
    Optional<Promotion> getByPromotionId(Long promotionId);
}
