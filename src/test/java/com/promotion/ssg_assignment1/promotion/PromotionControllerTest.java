package com.promotion.ssg_assignment1.promotion;

import com.promotion.ssg_assignment1.promotion.dto.CreatePromotionReq;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@WebAppConfiguration
public class PromotionControllerTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validatorInjected;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validatorInjected = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("[프로모션 입력 테스트]")
    void 프로모션_입력_테스트() {
        //given
        CreatePromotionReq createPromotionReq = new CreatePromotionReq(
                "", "2000", "",
                LocalDate.of(2022, 01, 01),
                LocalDate.of(2022, 02, 01)
        );
        //when
        Set<ConstraintViolation<CreatePromotionReq>> validate = validatorInjected.validate(createPromotionReq);

        Iterator<ConstraintViolation<CreatePromotionReq>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<CreatePromotionReq> next = iterator.next();
            messages.add(next.getMessage());
        }
        //then
        Assertions.assertThat(messages).contains("프로모션 이름은 공백일 수 없습니다.");
    }
}
