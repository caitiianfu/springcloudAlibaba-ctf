package com.unicom.rabbitmq.util;

import com.unicom.rabbitmq.annotation.IsMobile;
import com.unicom.rabbitmq.bo.Mail;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by ctf
 * @Classsname PhoneValidator
 * @Description TODO
 * @Date 2020/5/31 14:03
 **/
public class PhoneValidator implements ConstraintValidator<IsMobile, String> {
    private Pattern pattern=Pattern.compile("^1[345678]\\d{9}$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
          return true;
        }
        Matcher matcher=pattern.matcher(value);
        return matcher.matches();
    }
}
