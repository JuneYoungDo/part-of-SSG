package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.promotion.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String itemName;
    private String itemType;
    private double price;
    private LocalDate displayStartDate;
    private LocalDate displayEndDate;
    private boolean deleted;

    @ManyToMany
    @JoinTable(name = "promotion_item",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private List<Promotion> promotions;

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void changePromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}
