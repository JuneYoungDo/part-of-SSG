package com.promotion.ssg_assignment1.promotion;

import com.promotion.ssg_assignment1.item.Item;
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
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;
    private String promotionName;
    private double discountAmount;
    private double discountRate;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
    private boolean deleted;

    @ManyToMany
    @JoinTable(name = "promotion_item",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void changeItems(List<Item> items) {
        this.items = items;
    }

}
