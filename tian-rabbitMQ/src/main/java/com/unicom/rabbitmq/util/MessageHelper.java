package com.unicom.rabbitmq.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

/**
 * @author by ctf
 * @Classsname MessageHelper
 * @Description TODO
 * @Date 2020/5/28 10:54
 **/
public class MessageHelper {
    public  static Message objToMsg(Object obj){
        if (obj==null) {
            return  null;
        }
        Message message= MessageBuilder.withBody(JSONObject.toJSONString(obj).getBytes())
                .build();
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return  message;

    }
    public static <T> T msgToObj(Message message,Class<T> clazz){
        if (message==null||clazz==null){
            return null;
        }
        String str=new String(message.getBody());

        return JSONObject.parseObject(str,clazz);
    }
}
