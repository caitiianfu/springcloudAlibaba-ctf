package com.unicom.rabbitmq.bo;

import com.unicom.rabbitmq.annotation.IsMobile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author by ctf
 * @Classsname Mail
 * @Description TODO
 * @Date 2020/5/28 0:18
 **/
@Data
public class Mail {
    private String msgId;
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",message = "邮箱格式不正确")
    private String to;
    @NotBlank(message = "标题不能为空")
    private String subject;
    @NotBlank(message = "内容不能为空")
    private String content;
    @IsMobile
    private String phone;
}
