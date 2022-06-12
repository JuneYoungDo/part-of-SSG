package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.exception.BaseException;
import com.promotion.ssg_assignment1.exception.BaseResponse;
import com.promotion.ssg_assignment1.exception.BaseResponseStatus;
import com.promotion.ssg_assignment1.user.dto.CreateUserReq;
import com.promotion.ssg_assignment1.user.dto.DeleteUserReq;
import com.promotion.ssg_assignment1.user.dto.GetUserItemListReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 사용자 입력 API
     * [POST] /user
     */
    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody CreateUserReq createUserReq) throws BaseException {
        userService.newUser(createUserReq);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    /**
     * 사용자 삭제 API
     * [DELETE] /user
     */
    @DeleteMapping("/user")
    public ResponseEntity deleteUser(@RequestBody DeleteUserReq deleteUserReq) throws BaseException {
        userService.deleteUser(deleteUserReq);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    /**
     * 사용자가 구매할 수 있는 상품 정보
     * [GET] /user/item
     */
    @GetMapping("/user/item")
    public ResponseEntity getUserItemList(@RequestBody GetUserItemListReq getUserItemListReq) throws BaseException {
        return new ResponseEntity(userService.getUserItemList(getUserItemListReq), HttpStatus.OK);
    }
}
