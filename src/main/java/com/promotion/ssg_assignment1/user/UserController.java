package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponse;
import com.promotion.ssg_assignment1.user.Dto.CreateUserReq;
import com.promotion.ssg_assignment1.user.Dto.DeleteUserReq;
import com.promotion.ssg_assignment1.user.Dto.GetUserItemListReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 사용자 입력 API
     * [POST] /user
     */
    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody CreateUserReq createUserReq) {
        try {
            userService.newUser(createUserReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }

    /**
     * 사용자 삭제 API
     * [DELETE] /user
     */
    @DeleteMapping("/user")
    public ResponseEntity deleteUser(@RequestBody DeleteUserReq deleteUserReq) {
        try {
            userService.deleteUser(deleteUserReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }

    /**
     * 사용자가 구매할 수 있는 상품 정보
     * [GET] /user/item
     */
    @GetMapping("/user/item")
    public ResponseEntity getUserItemList(@RequestBody GetUserItemListReq getUserItemListReq) {
        try {
            return new ResponseEntity(userService.getUserItemList(getUserItemListReq), HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }
}
