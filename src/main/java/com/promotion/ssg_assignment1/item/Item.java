package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.promotion.Promotion;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
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
    @JoinTable(name= "promotion_item",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name= "promotion_id"))
    private List<Promotion> promotions;
}
