package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Dto.CreateItemReq;
import com.promotion.ssg_assignment1.item.Dto.DeleteItemReq;
import com.promotion.ssg_assignment1.item.Dto.GetItemPromotionReq;
import com.promotion.ssg_assignment1.item.Dto.GetItemPromotionRes;
import com.promotion.ssg_assignment1.promotion.Promotion;
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
public class ItemServiceTest {

    @InjectMocks
    ItemService itemService;
    @Mock
    ItemRepository itemRepository;

    Item item = Item.builder().itemId(1L).itemName("TEST_ITEM").itemType("TEST_TYPE").price(10000)
            .displayStartDate(LocalDate.of(2022, 01, 01))
            .displayEndDate(LocalDate.of(2022, 01, 02))
            .deleted(false).build();

    CreateItemReq createItemReq = new CreateItemReq(
            "TEST_ITEM", "TEST_TYPE", "10000",
            LocalDate.of(2022, 01, 01),
            LocalDate.of(2022, 01, 02)
    );

    @Test
    @DisplayName("[상품 생성 성공]")
    void 상품_생성_성공() throws BaseException {
        when(itemRepository.save(any())).then(returnsFirstArg());
        Item savedItem = itemService.newItem(createItemReq);
        assertEquals(item.getItemName(), savedItem.getItemName());
    }

    @Test
    @DisplayName("[상품 생성 실패] 가격이 올바르지 않은 경우")
    void 상품_생성_실패_가격() {
        createItemReq.setPrice("-1000");
        BaseException exception = assertThrows(BaseException.class,
                () -> itemService.newItem(createItemReq));
        assertThat(BaseResponseStatus.INVALID_PRICE.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[상품 생성 실패] 기간이 올바르지 않은 경우")
    void 상품_생성_실패_기간() {
        createItemReq.setDisplayEndDate(LocalDate.of(2021, 01, 01));
        BaseException exception = assertThrows(BaseException.class,
                () -> itemService.newItem(createItemReq));
        assertThat(BaseResponseStatus.INVALID_PERIOD.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[상품 삭제 성공]")
    void 상품_삭제_성공() throws BaseException {
        DeleteItemReq deleteItemReq = new DeleteItemReq(1L);
        when(itemRepository.getByItemId(1L)).thenReturn(Optional.ofNullable(item));
        Item deleteItem = itemService.deleteItem(deleteItemReq);
        assertEquals(true, deleteItem.isDeleted());
    }

    @Test
    @DisplayName("[상품 삭제 실패] itemId가 존재하지 않는 경우")
    void 상품_삭제_실패() {
        DeleteItemReq deleteItemReq = new DeleteItemReq(1L);
        BaseException exception = assertThrows(BaseException.class,
                () -> itemService.deleteItem(deleteItemReq));
        assertThat(BaseResponseStatus.INVALID_ITEM_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[상품 적용 프로모션 정보 가져오기 성공]")
    void 상품_적용_프로모션_정보_가져오기_성공() throws BaseException {
        GetItemPromotionReq getItemPromotionReq = new GetItemPromotionReq(1L);
        Promotion promotion = Promotion.builder().promotionId(1L).promotionName("TEST_PROMOTION")
                .discountAmount(1000).discountRate(0)
                .promotionStartDate(LocalDate.of(2022, 01, 01))
                .promotionEndDate(LocalDate.of(2022, 12, 01))
                .deleted(false).build();
        List<Promotion> promotionList = new ArrayList<>();
        promotionList.add(promotion);
        item.setPromotions(promotionList);
        when(itemRepository.getByItemId(1L)).thenReturn(Optional.ofNullable(item));
        GetItemPromotionRes getItemPromotionRes = itemService.getItemPromotion(getItemPromotionReq);
        assertEquals(9000, getItemPromotionRes.getDiscountPrice());
    }

    @Test
    @DisplayName("[상품 적용 프로모션 정보 가져오기 실패] itemId가 존재하지 않는 경우")
    void 상품_적용_프로모션_정보_가져오기_실패() {
        GetItemPromotionReq getItemPromotionReq = new GetItemPromotionReq(1L);
        BaseException exception = assertThrows(BaseException.class,
                () -> itemService.getItemPromotion(getItemPromotionReq));
        assertThat(BaseResponseStatus.INVALID_ITEM_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

}
