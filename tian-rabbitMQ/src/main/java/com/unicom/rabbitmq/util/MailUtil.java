package com.unicom.rabbitmq.util;

import com.unicom.rabbitmq.bo.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author by ctf
 * @Classsname MailUtil
 * @Description TODO
 * @Date 2020/5/28 11:23
 **/
@Component
@Slf4j
public class MailUtil {
    @Value("${spring.mail.from}")
    private String from;
    @Autowired
    private JavaMailSender sender;
    public Boolean sendMail(Mail mail){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(from);
        String to=mail.getTo();
        mailMessage.setTo(to);
        String subject=mail.getSubject();
        mailMessage.setSubject(subject);
        mailMessage.setText(mail.getContent());
        try {
            sender.send(mailMessage);
            log.info("发送成功");
            return true;
        }catch (Exception e){
            log.error("发送失败，from {} to {} title {}",from,to,subject);
            return  false;
        }
    }
}
