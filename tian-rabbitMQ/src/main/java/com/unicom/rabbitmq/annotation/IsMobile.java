package com.unicom.rabbitmq.annotation;

import com.unicom.rabbitmq.util.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author by ctf
 * @Classsname IsMobile
 * @Description TODO
 * @Date 2020/5/31 13:58
 **/
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface IsMobile {
    String message() default "手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
