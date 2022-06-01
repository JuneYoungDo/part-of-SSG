package com.promotion.ssg_assignment1.promotion;

import com.promotion.ssg_assignment1.item.Item;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;
    private String promotionName;
    private Long discountAmount;
    private double discountRate;
    private LocalDateTime promotionStartDate;
    private LocalDateTime promotionEndDate;

    @ManyToMany
    @JoinTable(name= "promotion_item",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;
}
