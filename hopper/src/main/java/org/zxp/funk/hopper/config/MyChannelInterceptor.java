package org.zxp.funk.hopper.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * Created by Administrator on 2017/6/13.
 */
public class MyChannelInterceptor extends ChannelInterceptorAdapter {
    
    @Override
    public boolean preReceive(MessageChannel channel) {
        System.out.println("preReceive");
        return super.preReceive(channel);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        //检测用户订阅内容（防止用户订阅不合法频道）
        
        System.out.println("命令="+command);
       
        return super.preSend(message, channel);
    }
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        //System.out.println("afterSendCompletion");
        //检测用户是否连接成功，搜集在线的用户信息如果数据量过大我们可以选择使用缓存数据库比如redis,
        //这里由于需要频繁的删除和增加集合内容，我们选择set集合来存储在线用户
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        System.out.println("命令="+command);
        if (StompCommand.CONNECT.equals(command)){
          

        }
        //如果用户断开连接，删除用户信息
        if (StompCommand.DISCONNECT.equals(command)){
            

        }
        super.afterSendCompletion(message, channel, sent, ex);
        if (StompCommand.SUBSCRIBE.equals(command)){
            

        }
    }

}