package com.promotion.ssg_assignment1.item;

import com.promotion.ssg_assignment1.item.dto.CreateItemReq;
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
public class ItemControllerTest {

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
    @DisplayName("[상품 입력 테스트]")
    void 상품_입력_테스트() {
        //given
        CreateItemReq createItemReq = new CreateItemReq(
                "", "TEST_TYPE", "",
                LocalDate.of(2022, 01, 01),
                LocalDate.of(2022, 01, 02)
        );
        //when
        Set<ConstraintViolation<CreateItemReq>> validate = validatorInjected.validate(createItemReq);

        Iterator<ConstraintViolation<CreateItemReq>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<CreateItemReq> next = iterator.next();
            messages.add(next.getMessage());
        }
        //then
        Assertions.assertThat(messages).contains("상품 이름은 공백일 수 없습니다."
                , "가격은 공백일 수 없습니다.");
    }
}
