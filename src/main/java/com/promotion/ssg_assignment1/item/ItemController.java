package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.exception.BaseException;
import com.promotion.ssg_assignment1.exception.BaseResponse;
import com.promotion.ssg_assignment1.exception.BaseResponseStatus;
import com.promotion.ssg_assignment1.item.dto.CreateItemReq;
import com.promotion.ssg_assignment1.item.dto.DeleteItemReq;
import com.promotion.ssg_assignment1.item.dto.GetItemPromotionReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createItem(@RequestBody @Valid CreateItemReq createItemReq, Errors errors) throws BaseException {
        if (errors.hasErrors()) {
            BaseResponseStatus baseResponseStatus = BaseResponseStatus.CUSTOM_ERROR;
            baseResponseStatus.setMessage(errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(new BaseResponse(baseResponseStatus), HttpStatus.valueOf(baseResponseStatus.getStatus()));
        }
        itemService.newItem(createItemReq);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    /**
     * 상품 삭제 API
     * [DELETE] /item
     */
    @DeleteMapping("/item")
    public ResponseEntity deleteItem(@RequestBody DeleteItemReq deleteItemReq) throws BaseException {
        itemService.deleteItem(deleteItemReq);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    /**
     * 상품에 적용된 프로모션 정보 가져오기
     * [GET] /item/promotion
     */
    @GetMapping("/item/promotion")
    public ResponseEntity getItemPromotion(@RequestBody GetItemPromotionReq getItemPromotionReq) throws BaseException {
        return new ResponseEntity(itemService.getItemPromotion(getItemPromotionReq), HttpStatus.OK);
    }
}
