package org.zxp.funk.hopper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.zxp.funk.hopper.jpa.repository.ServerOperationRepository;
import org.zxp.funk.hopper.service.ServerList;  
  

@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {  
  
	@Autowired
	ServerList serverlist;
	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
        registry.addEndpoint("/hopperserver").withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	// TODO Auto-generated method stub
    	
    	registration.setInterceptors(myChannelInterceptor());
    	super.configureClientInboundChannel(registration);
    }
    
    @Bean
    public ChannelInterceptorAdapter myChannelInterceptor() {
    	ChannelInterceptorAdapter my = new ChannelInterceptorAdapter() {
    		@Override
    		public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent,
    				Exception ex) {
    			 StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    		        StompCommand command = accessor.getCommand();
    			super.afterSendCompletion(message, channel, sent, ex);
		        if (StompCommand.SUBSCRIBE.equals(command)){
		            
		        	brokerMessagingTemplate.convertAndSend("/topic/serverstatus", serverlist.getAll());
		        }
    		}
		};
		return my;
    }
    
}  
