package org.zxp.funk.hopper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.zxp.funk.hopper.service.ServerList;



//@Component
public class WebSocketEvents {

	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;
	
	@Autowired
	private ServerList serverlist;
	
	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		System.out.println("www");
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", serverlist.getAll());
		
	}

	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		System.out.println("duan");
	}
	
	@EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
		System.out.println("dingyue");
		brokerMessagingTemplate.convertAndSend("/topic/serverstatus", serverlist.getAll());
	}
}