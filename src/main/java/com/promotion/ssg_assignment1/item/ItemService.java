package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Dto.CreateItemReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    public Item createItem(String name, String type, double price, LocalDate start, LocalDate end) {
        Item item = Item.builder()
                .itemName(name)
                .itemType(type)
                .price(price)
                .displayStartDate(start)
                .displayEndDate(end)
                .deleted(false)
                .build();
        save(item);
        return item;
    }

    public Item newItem(CreateItemReq createItemReq) throws BaseException {
        LocalDate startDate = createItemReq.getDisplayStartDate();
        LocalDate endDate = createItemReq.getDisplayEndDate();
        Long diff = ChronoUnit.DAYS.between(startDate, endDate);
        double price = Double.parseDouble(createItemReq.getPrice());
        if (price <= 0)
            throw new BaseException(BaseResponseStatus.INVALID_PRICE);
        if (diff < 0)
            throw new BaseException(BaseResponseStatus.INVALID_PERIOD);
        String type = "일반";
        if (createItemReq.getType().equals("기업회원상품")) type = "기업회원상품";
        return createItem(createItemReq.getName(), type, price, startDate, endDate);
    }
}
