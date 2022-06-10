package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.exception.BaseException;
import com.promotion.ssg_assignment1.exception.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Item;
import com.promotion.ssg_assignment1.item.ItemRepository;
import com.promotion.ssg_assignment1.user.dto.CreateUserReq;
import com.promotion.ssg_assignment1.user.dto.DeleteUserReq;
import com.promotion.ssg_assignment1.user.dto.GetUserItemListReq;
import com.promotion.ssg_assignment1.user.dto.GetUserItemListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User createUser(String name, String type) {
        User user = User.builder()
                .name(name)
                .type(type)
                .deleted(false)
                .build();
        save(user);
        return user;
    }

    public User newUser(CreateUserReq createUserReq) throws BaseException {
        if (createUserReq.getName().length() == 0 || createUserReq.getName().equals(" ")) {
            throw new BaseException(BaseResponseStatus.EMPTY_NAME);
        }
        String type = "일반";
        if (createUserReq.getType().equals("기업회원")) {
            type = "기업회원";
        }
        return createUser(createUserReq.getName(), type);
    }

    @Transactional
    public User deleteUser(DeleteUserReq deleteUserReq) throws BaseException {
        User user = userRepository.getByUserId(deleteUserReq.getUserId()).orElse(null);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_ID);
        }
        user.changeDeleted(true);
        return user;
    }

    public List<GetUserItemListRes> getUserItemList(GetUserItemListReq getUserItemListReq) throws BaseException {
        User user = userRepository.getByUserId(getUserItemListReq.getUserId()).orElse(null);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_ID);
        }
        LocalDate localDate = LocalDate.now();
        List<Item> itemList;
        if (user.getType().equals("기업회원")) {
            itemList = itemRepository.getAllItemsUseDate(localDate).orElse(new ArrayList<>());
        } else {
            itemList = itemRepository.getAllItemsUseDateWithType(localDate, "일반").orElse(new ArrayList<>());
        }
        List<GetUserItemListRes> ansList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Item tmpItem = itemList.get(i);
            ansList.add(new GetUserItemListRes(tmpItem.getItemId(), tmpItem.getItemName(), tmpItem.getItemType(),
                    tmpItem.getPrice(), tmpItem.getDisplayStartDate(), tmpItem.getDisplayEndDate()));
        }
        return ansList;
    }
}
