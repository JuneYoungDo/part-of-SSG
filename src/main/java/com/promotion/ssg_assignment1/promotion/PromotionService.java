package com.promotion.ssg_assignment1.promotion;

import com.promotion.ssg_assignment1.exception.BaseException;
import com.promotion.ssg_assignment1.exception.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Item;
import com.promotion.ssg_assignment1.item.ItemRepository;
import com.promotion.ssg_assignment1.promotion.dto.ApplyItemToPromotionReq;
import com.promotion.ssg_assignment1.promotion.dto.CreatePromotionReq;
import com.promotion.ssg_assignment1.promotion.dto.DeletePromotionReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public Promotion createPromotion(String name, double discountAmount, double discountRate, LocalDate startDate, LocalDate endDate) {
        Promotion promotion = Promotion.builder()
                .promotionName(name)
                .discountAmount(discountAmount)
                .discountRate(discountRate)
                .promotionStartDate(startDate)
                .promotionEndDate(endDate)
                .deleted(false)
                .build();
        save(promotion);
        return promotion;
    }

    public Promotion newPromotion(CreatePromotionReq createPromotionReq) throws BaseException {
        if (createPromotionReq.getDiscountAmount().length() == 0 && createPromotionReq.getDiscountRate().length() == 0) {
            throw new BaseException(BaseResponseStatus.CAN_NOT_EMPTY_BOTH_OF_DISCOUNT);
        }
        if (createPromotionReq.getDiscountAmount().length() != 0 && createPromotionReq.getDiscountRate().length() != 0) {
            throw new BaseException(BaseResponseStatus.CAN_NOT_BOTH_OF_DISCOUNT);
        }
        LocalDate startDate = createPromotionReq.getPromotionStartDate();
        LocalDate endDate = createPromotionReq.getPromotionEndDate();
        double discountAmount = 0, discountRate = 0;
        if (createPromotionReq.getDiscountAmount().length() == 0) {
            discountRate = Double.parseDouble(createPromotionReq.getDiscountRate());
            if (discountRate <= 0) {
                throw new BaseException(BaseResponseStatus.INVALID_DISCOUNT);
            }
        } else {
            discountAmount = Double.parseDouble(createPromotionReq.getDiscountAmount());
            if (discountAmount <= 0) {
                throw new BaseException(BaseResponseStatus.INVALID_DISCOUNT);
            }
        }
        Long diff = ChronoUnit.DAYS.between(startDate, endDate);
        if (diff < 0) {
            throw new BaseException(BaseResponseStatus.INVALID_PERIOD);
        }
        return createPromotion(createPromotionReq.getName(), discountAmount, discountRate, startDate, endDate);
    }

    @Transactional
    public Promotion deletePromotion(DeletePromotionReq deletePromotionReq) throws BaseException {
        Promotion promotion = promotionRepository.getByPromotionId(deletePromotionReq.getPromotionId()).orElse(null);
        if (promotion == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PROMOTION_ID);
        }
        promotion.changeDeleted(true);
        return promotion;
    }

    @Transactional
    public Promotion applyItemToPromotion(ApplyItemToPromotionReq applyItemToPromotionReq) throws BaseException {
        Promotion promotion = promotionRepository.getByPromotionId(applyItemToPromotionReq.getPromotionId()).orElse(null);
        if (promotion == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PROMOTION_ID);
        }
        Item item = itemRepository.getByItemId(applyItemToPromotionReq.getItemId()).orElse(null);
        if (item == null) {
            throw new BaseException(BaseResponseStatus.INVALID_ITEM_ID);
        }
        List<Item> itemList = promotion.getItems();
        if (itemList.contains(item)) {
            throw new BaseException(BaseResponseStatus.ALREADY_CONTAINS);
        }
        itemList.add(item);
        promotion.changeItems(itemList);
        return promotion;
    }
}
