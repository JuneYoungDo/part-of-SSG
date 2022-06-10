package com.promotion.ssg_assignment1.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select i from Item i where i.itemId = :itemId and i.deleted = false")
    Optional<Item> getByItemId(Long itemId);

    @Query(value = "select i from Item i where i.deleted = false " +
            "and i.displayStartDate <= :nowDate and i.displayEndDate >= :nowDate")
    Optional<List<Item>> getAllItemsUseDate(LocalDate nowDate);

    @Query(value = "select i from Item i where i.deleted = false" +
            " and i.displayStartDate <= :nowDate and i.displayEndDate >= :nowDate and i.itemType = :type")
    Optional<List<Item>> getAllItemsUseDateWithType(LocalDate nowDate, String type);
}
