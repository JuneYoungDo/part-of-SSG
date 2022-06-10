package com.promotion.ssg_assignment1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserItemListRes {
    private Long itemId;
    private String name;
    private String type;
    private double price;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
}
