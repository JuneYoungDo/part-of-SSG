package com.promotion.ssg_assignment1.promotion;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponse;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.promotion.Dto.ApplyItemToPromotionReq;
import com.promotion.ssg_assignment1.promotion.Dto.CreatePromotionReq;
import com.promotion.ssg_assignment1.promotion.Dto.DeletePromotionReq;
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
public class PromotionController {
    private final PromotionService promotionService;

    /**
     * 프로모션 입력 API
     * [POST] /promotion
     */
    @PostMapping("/promotion")
    public ResponseEntity createPromotion(@RequestBody @Valid CreatePromotionReq createPromotionReq, Errors errors) {
        if (errors.hasErrors()) {
            BaseResponseStatus baseResponseStatus = BaseResponseStatus.CUSTOM_ERROR;
            baseResponseStatus.setMessage(errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(new BaseResponse(baseResponseStatus), HttpStatus.valueOf(baseResponseStatus.getStatus()));
        }
        try {
            promotionService.newPromotion(createPromotionReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }

    /**
     * 프로모션 삭제 API
     * [DELETE] /promotion
     */
    @DeleteMapping("/promotion")
    public ResponseEntity deletePromotion(@RequestBody DeletePromotionReq deletePromotionReq) {
        try {
            promotionService.deletePromotion(deletePromotionReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }

    /**
     * 프로모션에 상품 적용 API
     * [POST] /promotion/item
     */
    @PostMapping("/promotion/item")
    public ResponseEntity applyItemToPromotion(@RequestBody ApplyItemToPromotionReq applyItemToPromotionReq) {
        try {
            promotionService.applyItemToPromotion(applyItemToPromotionReq);
            return new ResponseEntity(200, HttpStatus.valueOf(200));
        } catch (BaseException exception) {
            return new ResponseEntity(new BaseResponse(exception.getStatus()),
                    HttpStatus.valueOf(exception.getStatus().getStatus()));
        }
    }
}
