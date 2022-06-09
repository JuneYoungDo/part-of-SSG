package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Item;
import com.promotion.ssg_assignment1.item.ItemRepository;
import com.promotion.ssg_assignment1.user.Dto.CreateUserReq;
import com.promotion.ssg_assignment1.user.Dto.DeleteUserReq;
import com.promotion.ssg_assignment1.user.Dto.GetUserItemListReq;
import com.promotion.ssg_assignment1.user.Dto.GetUserItemListRes;
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
public class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    User user = User.builder().userId(1L).name("TEST").type("TYPE").deleted(false).build();

    @Test
    @DisplayName("[유저 생성 성공]")
    void 유저_생성_성공() throws BaseException {
        CreateUserReq createUserReq = new CreateUserReq("TEST", "TYPE");
        when(userRepository.save(any())).then(returnsFirstArg());
        User savedUser = userService.newUser(createUserReq);
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    @DisplayName("[유저 생성 실패] 이름이 비어있는 경우")
    void 유저_생성_실패() {
        CreateUserReq createUserReq = new CreateUserReq("", "");
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.newUser(createUserReq));
        assertThat(BaseResponseStatus.EMPTY_NAME.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[유저 삭제 성공]")
    void 유저_삭제_성공() throws BaseException {
        DeleteUserReq deleteUserReq = new DeleteUserReq(1L);
        when(userRepository.getByUserId(1L)).thenReturn(Optional.ofNullable(user));
        User deleteUser = userService.deleteUser(deleteUserReq);
        assertEquals(true, deleteUser.isDeleted());
    }

    @Test
    @DisplayName("[유저 삭제 실패] userId가 존재하지 않는 경우")
    void 유저_삭제_실패() {
        DeleteUserReq deleteUserReq = new DeleteUserReq(1L);
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.deleteUser(deleteUserReq));
        assertThat(BaseResponseStatus.INVALID_USER_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[유저 구매 가능 상품 가져오기 성공]")
    void 유저_구매_가능_상품_가져오기_성공() throws BaseException {
        Item item = Item.builder().itemId(1L).itemName("TEST_ITEM").itemType("TEST_TYPE").price(10000)
                .displayStartDate(LocalDate.of(2022, 01, 01))
                .displayEndDate(LocalDate.of(2022, 01, 02))
                .deleted(false).build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(userRepository.getByUserId(any())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.getAllItemsUseDate(any())).thenReturn(Optional.ofNullable(itemList));
        List<GetUserItemListRes> ansList = userService.getUserItemList(new GetUserItemListReq(2L));
        assertEquals(1L, ansList.get(0).getItemId());
    }

    @Test
    @DisplayName("[유저 구매가능 상품 가져오기 실패] userId가 존재하지 않는 경우")
    void 유저_구매_가능_상품_가져오기_실패() {
        GetUserItemListReq getUserItemListReq = new GetUserItemListReq(1L);
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.getUserItemList(getUserItemListReq));
        assertThat(BaseResponseStatus.INVALID_USER_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

}
