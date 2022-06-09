package com.promotion.ssg_assignment1.promotion;


import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Item;
import com.promotion.ssg_assignment1.item.ItemRepository;
import com.promotion.ssg_assignment1.promotion.Dto.ApplyItemToPromotionReq;
import com.promotion.ssg_assignment1.promotion.Dto.CreatePromotionReq;
import com.promotion.ssg_assignment1.promotion.Dto.DeletePromotionReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
    @InjectMocks
    PromotionService promotionService;
    @Mock
    PromotionRepository promotionRepository;
    @Mock
    ItemRepository itemRepository;

    Promotion promotion = Promotion.builder().promotionId(1L).promotionName("TEST_PROMOTION")
            .discountAmount(1000).discountRate(0)
            .promotionStartDate(LocalDate.of(2022, 01, 01))
            .promotionEndDate(LocalDate.of(2022, 02, 01))
            .deleted(false).build();

    CreatePromotionReq createPromotionReq = new CreatePromotionReq(
            "TEST_PROMOTION", "2000", "",
            LocalDate.of(2022, 01, 01),
            LocalDate.of(2022, 02, 01)
    );

    Item item = Item.builder().itemId(1L).itemName("TEST_ITEM").itemType("TEST_TYPE").price(10000)
            .displayStartDate(LocalDate.of(2022, 01, 01))
            .displayEndDate(LocalDate.of(2022, 01, 02)).build();

    @Test
    @DisplayName("[프로모션 생성 성공]")
    void 프로모션_생성_성공() throws BaseException {
        when(promotionRepository.save(any())).then(returnsFirstArg());
        Promotion savedPromotion = promotionService.newPromotion(createPromotionReq);
        assertEquals(promotion.getPromotionName(), savedPromotion.getPromotionName());
    }

    @Test
    @DisplayName("[프로모션 생성 실패] 할인금액과 할인율이 모두 존재하는 경우")
    void 상품_생성_실패_할인율_할인금액() {
        createPromotionReq.setDiscountRate("50");
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.newPromotion(createPromotionReq));
        assertThat(BaseResponseStatus.CAN_NOT_BOTH_OF_DISCOUNT.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 생성 실패] 할인금액과 할인율이 모두 존재하지 않는 경우")
    void 상품_생성_실패_할인율_할인금액_없음() {
        createPromotionReq.setDiscountAmount("");
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.newPromotion(createPromotionReq));
        assertThat(BaseResponseStatus.CAN_NOT_EMPTY_BOTH_OF_DISCOUNT.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 생성 실패] 할인금액 혹은 할인율이 올바르지 않는 경우")
    void 상품_생성_실패_가격() {
        createPromotionReq.setDiscountAmount("-1000");
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.newPromotion(createPromotionReq));
        assertThat(BaseResponseStatus.INVALID_DISCOUNT.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 생성 실패] 행사 기간이 잘못된 경우")
    void 상품_생성_실패_기간() {
        createPromotionReq.setPromotionEndDate(LocalDate.of(2021, 02, 01));
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.newPromotion(createPromotionReq));
        assertThat(BaseResponseStatus.INVALID_PERIOD.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 삭제 성공]")
    void 프로모션_삭제_성공() throws BaseException {
        DeletePromotionReq deletePromotionReq = new DeletePromotionReq(1L);
        when(promotionRepository.getByPromotionId(1L)).thenReturn(Optional.ofNullable(promotion));
        Promotion deletedPromotion = promotionService.deletePromotion(deletePromotionReq);
        assertEquals(true, deletedPromotion.isDeleted());
    }

    @Test
    @DisplayName("[프로모션 삭제 실패] promotionId가 존재하지 않는 경우")
    void 프로모션_삭제_실패() {
        DeletePromotionReq deletePromotionReq = new DeletePromotionReq(1L);
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.deletePromotion(deletePromotionReq));
        assertThat(BaseResponseStatus.INVALID_PROMOTION_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 상품 적용 성공]")
    void 프로모션_상품_적용_성공() throws BaseException {
        ApplyItemToPromotionReq applyItemToPromotionReq = new ApplyItemToPromotionReq(2L, 2L);
        promotion.setItems(new ArrayList<>());
        when(promotionRepository.getByPromotionId(any())).thenReturn(Optional.ofNullable(promotion));
        when(itemRepository.getByItemId(any())).thenReturn(Optional.ofNullable(item));
        Promotion applyItemToPromotion = promotionService.applyItemToPromotion(applyItemToPromotionReq);
        assertEquals(1, applyItemToPromotion.getItems().size());
    }

    @Test
    @DisplayName("[프로모션 상품 적용 실패] promotionId가 존재하지 않는 경우")
    void 프로모션_상품_적용_실패_프로모션_아이디() {
        ApplyItemToPromotionReq applyItemToPromotionReq = new ApplyItemToPromotionReq(2L, 2L);
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.applyItemToPromotion(applyItemToPromotionReq));
        assertThat(BaseResponseStatus.INVALID_PROMOTION_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[프로모션 상품 적용 실패] itemId가 존재하지 않는 경우")
    void 프로모션_상품_적용_실패_상품_아이디() {
        when(promotionRepository.getByPromotionId(any())).thenReturn(Optional.ofNullable(promotion));
        ApplyItemToPromotionReq applyItemToPromotionReq = new ApplyItemToPromotionReq(2L, 2L);
        BaseException exception = assertThrows(BaseException.class,
                () -> promotionService.applyItemToPromotion(applyItemToPromotionReq));
        assertThat(BaseResponseStatus.INVALID_ITEM_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }
}
