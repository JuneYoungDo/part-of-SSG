package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponse;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.Dto.CreateItemReq;
import com.promotion.ssg_assignment1.item.Dto.DeleteItemReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * 상품 입력 API
     * [POST] /item
     */
    @PostMapping("/item")
    public ResponseEntity createItem(@RequestBody @Valid CreateItemReq createItemReq, Errors errors) {
        if (errors.hasErrors()) {
            BaseResponseStatus baseResponseStatus = BaseResponseStatus.CUSTOM_ERROR;
            baseResponseStatus.setMessage(errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(new BaseResponse(baseResponseStatus), HttpStatus.valueOf(baseResponseStatus.getStatus()));
        }
        try {
            itemService.newItem(createItemReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }

    /**
     * 상품 삭제 API
     * [DELETE] /item
     */
    @DeleteMapping("/item")
    public ResponseEntity deleteItem(@RequestBody DeleteItemReq deleteItemReq) {
        try {
            itemService.deleteItem(deleteItemReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }
}
