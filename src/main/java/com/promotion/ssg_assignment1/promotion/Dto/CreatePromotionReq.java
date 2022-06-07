package com.promotion.ssg_assignment1.promotion.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePromotionReq {
    @NotBlank(message = "프로모션 이름은 공백일 수 없습니다.")
    private String name;
    private String discountAmount;
    private String discountRate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionEndDate;
}
