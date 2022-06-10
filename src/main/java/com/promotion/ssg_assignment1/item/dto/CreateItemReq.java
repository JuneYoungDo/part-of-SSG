package com.promotion.ssg_assignment1.item.dto;

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
public class CreateItemReq {
    @NotBlank(message = "상품 이름은 공백일 수 없습니다.")
    private String name;
    private String type;
    @NotBlank(message = "가격은 공백일 수 없습니다.")
    private String price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate displayStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate displayEndDate;
}
