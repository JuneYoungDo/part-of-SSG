package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Dto.*;
import com.promotion.ssg_assignment1.promotion.Promotion;
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

    public boolean isValidItemId(Long itemId) {
        Item item = itemRepository.getByItemId(itemId).orElse(null);
        if (item == null || item.isDeleted()) return false;
        else return true;
    }

    @Transactional
    public Item deleteItem(DeleteItemReq deleteItemReq) throws BaseException {
        if (!isValidItemId(deleteItemReq.getItemId()))
            throw new BaseException(BaseResponseStatus.INVALID_ITEM_ID);
        Item item = itemRepository.getByItemId(deleteItemReq.getItemId()).orElse(null);
        item.setDeleted(true);
        return item;
    }

    public boolean isProperTerm(LocalDate startDate, LocalDate endDate, LocalDate nowDate) {
        Long diffBefore = ChronoUnit.DAYS.between(startDate, nowDate);
        Long diffAfter = ChronoUnit.DAYS.between(nowDate, endDate);
        if (diffBefore < 0 || diffAfter < 0) return false;
        else return true;
    }

    public PromotionPriceDto checkFirstPromotion(double originPrice, Promotion promotion) {
        double tmpPrice;
        if (promotion.getDiscountAmount() == 0) tmpPrice = originPrice - (originPrice * promotion.getDiscountRate());
        else tmpPrice = originPrice - promotion.getDiscountAmount();
        if (tmpPrice <= 0) return new PromotionPriceDto(null, 0);
        else return new PromotionPriceDto(promotion, tmpPrice);
    }

    public PromotionPriceDto comparePromotion(Promotion nowBest, double bestPrice,
                                              Promotion cmpPromotion, double originPrice) {
        double tmpPrice;
        if (cmpPromotion.getDiscountAmount() == 0)
            tmpPrice = originPrice - (originPrice * cmpPromotion.getDiscountRate());
        else tmpPrice = originPrice - cmpPromotion.getDiscountAmount();
        if (tmpPrice <= 0 || bestPrice < tmpPrice) return new PromotionPriceDto(nowBest, bestPrice);
        else return new PromotionPriceDto(cmpPromotion, tmpPrice);
    }

    public PromotionPriceDto getBestPrice(Item item) {
        PromotionPriceDto promotionPriceDto = new PromotionPriceDto(null, 0);
        double originPrice = item.getPrice(), promotionPrice = originPrice;
        for (int i = 0; i < item.getPromotions().size(); i++) {
            Promotion tmpPromotion = item.getPromotions().get(i);
            if (tmpPromotion.isDeleted() || !isProperTerm(tmpPromotion.getPromotionStartDate(), tmpPromotion.getPromotionEndDate(), LocalDate.now()))
                continue;
            if (promotionPriceDto.getPromotion() == null)
                promotionPriceDto = checkFirstPromotion(originPrice, tmpPromotion);
            else promotionPriceDto = comparePromotion(
                    promotionPriceDto.getPromotion(), promotionPriceDto.getPrice(), tmpPromotion, originPrice);
        }
        return promotionPriceDto;
    }

    public GetItemPromotionRes getItemPromotion(GetItemPromotionReq getItemPromotionReq) throws BaseException {
        if (!isValidItemId(getItemPromotionReq.getItemId()))
            throw new BaseException(BaseResponseStatus.INVALID_ITEM_ID);
        Item item = itemRepository.getByItemId(getItemPromotionReq.getItemId()).orElse(null);
        PromotionPriceDto promotionPriceDto = getBestPrice(item);
        if (promotionPriceDto.getPromotion() == null) return null;
        else {
            Promotion promotion = promotionPriceDto.getPromotion();
            return new GetItemPromotionRes(promotion.getPromotionId(), promotion.getPromotionName(),
                    item.getPrice(), promotionPriceDto.getPrice(), promotion.getPromotionStartDate(), promotion.getPromotionEndDate());
        }
    }
}
