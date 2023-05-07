package com.list.springstomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    //Client에서 websocket연결할 때 사용할 API 경로를 설정해주는 메서드.
    //roomdetail.html의 46번째 줄, var sock = new SockJS("/ws/chat");
    //에서 새로운 핸드쉐이크 커넥션을 생성할 때 사용됨.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();

    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //메시지 받을 때 관련 경로 설정
        //"/queue", "/topic" 이 두 경로가 prefix(api 경로 맨 앞)에 붙은 경우, messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌
        //주로 "/queue"는 1대1 메시징, "/topic"은 1대다 메시징일 때 주로 사용함.
        registry.enableSimpleBroker("/queue", "/topic");

        //메시지 보낼 때 관련 경로 설정
        registry.setApplicationDestinationPrefixes("/app");
    }
}
