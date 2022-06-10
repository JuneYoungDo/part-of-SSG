package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.exception.BaseException;
import com.promotion.ssg_assignment1.exception.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Item;
import com.promotion.ssg_assignment1.item.ItemRepository;
import com.promotion.ssg_assignment1.user.dto.CreateUserReq;
import com.promotion.ssg_assignment1.user.dto.DeleteUserReq;
import com.promotion.ssg_assignment1.user.dto.GetUserItemListReq;
import com.promotion.ssg_assignment1.user.dto.GetUserItemListRes;
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

    //given
    User user = User.builder().userId(1L).name("TEST").type("TYPE").deleted(false).build();

    @Test
    @DisplayName("[유저 생성 성공]")
    void 유저_생성_성공() throws BaseException {
        //given
        CreateUserReq createUserReq = new CreateUserReq("TEST", "TYPE");
        //when
        when(userRepository.save(any())).then(returnsFirstArg());
        User savedUser = userService.newUser(createUserReq);
        //then
        assertEquals(user.getName(), savedUser.getName());
    }

    @Test
    @DisplayName("[유저 생성 실패] 이름이 비어있는 경우")
    void 유저_생성_실패() {
        //given
        CreateUserReq createUserReq = new CreateUserReq("", "");
        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.newUser(createUserReq));
        //then
        assertThat(BaseResponseStatus.EMPTY_NAME.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[유저 삭제 성공]")
    void 유저_삭제_성공() throws BaseException {
        //given
        DeleteUserReq deleteUserReq = new DeleteUserReq(1L);
        //when
        when(userRepository.getByUserId(1L)).thenReturn(Optional.ofNullable(user));
        User deleteUser = userService.deleteUser(deleteUserReq);
        //then
        assertEquals(true, deleteUser.isDeleted());
    }

    @Test
    @DisplayName("[유저 삭제 실패] userId가 존재하지 않는 경우")
    void 유저_삭제_실패() {
        //given
        DeleteUserReq deleteUserReq = new DeleteUserReq(1L);
        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.deleteUser(deleteUserReq));
        //then
        assertThat(BaseResponseStatus.INVALID_USER_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

    @Test
    @DisplayName("[유저 구매 가능 상품 가져오기 성공]")
    void 유저_구매_가능_상품_가져오기_성공() throws BaseException {
        //given
        Item item = Item.builder().itemId(1L).itemName("TEST_ITEM").itemType("TEST_TYPE").price(10000)
                .displayStartDate(LocalDate.of(2022, 01, 01))
                .displayEndDate(LocalDate.of(2022, 01, 02))
                .deleted(false).build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        //when
        when(userRepository.getByUserId(any())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.getAllItemsUseDateWithType(any(), any())).thenReturn(Optional.ofNullable(itemList));
        List<GetUserItemListRes> ansList = userService.getUserItemList(new GetUserItemListReq(2L));
        //then
        assertEquals(1L, ansList.get(0).getItemId());
    }

    @Test
    @DisplayName("[유저 구매 가능 상품 가져오기 실패] userId가 존재하지 않는 경우")
    void 유저_구매_가능_상품_가져오기_실패() {
        //given
        GetUserItemListReq getUserItemListReq = new GetUserItemListReq(1L);
        //when
        BaseException exception = assertThrows(BaseException.class,
                () -> userService.getUserItemList(getUserItemListReq));
        //then
        assertThat(BaseResponseStatus.INVALID_USER_ID.getMessage()).isEqualTo(exception.getStatus().getMessage());
    }

}
