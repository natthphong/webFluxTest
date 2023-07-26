package com.webflux.video.message;

import com.webflux.video.controller.rest.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class WebSocketConfiguration {
    @Autowired
    private MessageRepository messageRepository;
    @Bean
    public SimpleUrlHandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping(Map.of("/ws/{roomNumber}", wsh), 1);
    }
    @Bean
    @Primary
    public WebSocketHandler customHandler() {
        return new CustomHandle(messageRepository);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
