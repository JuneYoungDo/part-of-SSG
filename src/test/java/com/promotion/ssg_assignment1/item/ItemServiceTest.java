package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Dto.CreateItemReq;
import com.promotion.ssg_assignment1.item.Dto.DeleteItemReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
}
