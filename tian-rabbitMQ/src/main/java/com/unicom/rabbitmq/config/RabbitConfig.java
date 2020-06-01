package com.unicom.rabbitmq.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unicom.generator.entity.MsgLog;
import com.unicom.rabbitmq.service.IMsgLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by ctf
 * @Classsname RabbitConfig
 * @Description TODO
 * @Date 2020/5/27 22:40
 **/
@Configuration
@Slf4j
public class RabbitConfig {
    /**消息投递**/
    public  static final int MSG_DELIVERING=0;
    /**投递成功**/
    public  static final int MSG_DELIVERING_SUCCESS=1;
    /**消费成功**/
    public  static final int MSG_CONSUMER_SUCCESS=2;
    /**消费失败**/
    public  static final int MSG_CONSUMER_FAILED=3;

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private IMsgLogService msgLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback((corr,ack,cause)->{
            if (ack){
                log.info("消息发送到exchange成功");

                String msgId=corr.getId();
                QueryWrapper<MsgLog> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("msg_id",msgId);
                MsgLog msgLog=msgLogService.getOne(queryWrapper);
                //发送到exchange成功
                msgLog.setStatus(RabbitConfig.MSG_DELIVERING_SUCCESS);
                msgLogService.update(msgLog,queryWrapper);
            }else{
                log.error("发送到exchange失败,{},cause:{}",corr,cause);
            }
        });

        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息从exchange路由到queue失败，exchange {}，route {},replyCode {},replyText {},message {}",exchange,routingKey,replyCode,replyText,message);
        }));
        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    public  static  final String MAIL_QUEUE_NAME="mail.queue";
    public  static  final String MAIL_ROUTE_KEY="mail.route";
    public  static  final String MAIL_EXCHANGE="mail.exchange";
    @Bean
    public Queue mailQueue(){
        return  new Queue(MAIL_QUEUE_NAME,true);
    }
    @Bean
    public DirectExchange mailExchange(){
        return new DirectExchange(MAIL_EXCHANGE,true,false);
    }
    @Bean
    public Binding mailBuilding(){
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTE_KEY);
    }
}
