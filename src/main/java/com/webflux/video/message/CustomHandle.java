package com.webflux.video.message;

import com.webflux.video.MessageEntity;
import com.webflux.video.controller.rest.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CustomHandle implements WebSocketHandler {

//    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    private final MessageRepository messageRepository;

    public CustomHandle(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
////        var f = Flux.just("A","B","C","D","E").map(e->session.textMessage(e));
//        var f = session.receive()
//                .map(e-> e.getPayloadAsText())
//                .map(e-> new StringBuilder(e).reverse())
//                .map(e-> session.textMessage(e.toString())) ;
//        return session.send(f);
//    }


//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        String roomNumber = getRoomNumber(session);
//        roomSessions.computeIfAbsent(roomNumber, k -> ConcurrentHashMap.newKeySet()).add(session);
//        var f = session.receive()
//                .map(e -> e.getPayloadAsText())
//                .map(e -> new StringBuilder(e).reverse())
//                .map(e -> session.textMessage(e.toString()));
//
//        return session.send(f)
//                .doFinally(signal -> {
//                    roomSessions.getOrDefault(roomNumber, Collections.emptySet()).remove(session);
//                });
//    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        String query = session.getHandshakeInfo().getUri().getQuery();
        MultiValueMap<String, String> queryParams = parseQueryParams(query);
        String senderUsername = queryParams.getFirst("username");
        log.info("senderUsername {}" , senderUsername);
        String roomNumber = getRoomNumber(session);
//        roomSessions.computeIfAbsent(roomNumber, k -> ConcurrentHashMap.newKeySet()).add(session);
        Flux<MessageEntity> existingMessages = messageRepository.findByRoomNumberAndIsDeleted(roomNumber,"N");
//        Flux<WebSocketMessage> messageFlux = existingMessages
//                .map(MessageEntity::getContent)
//                .map(content -> new StringBuilder(content).toString())
//                .map(session::textMessage);
        Flux<WebSocketMessage> messageFlux = existingMessages
                .map(message -> {
                    if (senderUsername != null && senderUsername.equals(message.getUsername())) {
                        return message.getContent();
                    } else {
                        return new StringBuilder(message.getContent()).reverse().toString();
                    }
                })
                .map(session::textMessage);
        Flux<WebSocketMessage> incomingMessageFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText).map(e-> {
                    saveMessageToDB(roomNumber,e,senderUsername,sessionId);
                    return  session.textMessage(e);
                });
        Flux<WebSocketMessage> outgoingMessageFlux = Flux.merge(messageFlux, incomingMessageFlux);
        return session.send(outgoingMessageFlux);
//                .doFinally(signal -> {
//                    roomSessions.getOrDefault(roomNumber, Collections.emptySet()).remove(session);
//                });
    }

    private void saveMessageToDB(String roomNumber, String content,String username,String sessionId) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreateDate(LocalDateTime.now());
        messageEntity.setIsDeleted("N");
        messageEntity.setUsername(username);
        messageEntity.setSessionId(sessionId);
        messageEntity.setRoomNumber(roomNumber);
        messageEntity.setContent(content);
        Mono<MessageEntity> savedMessageMono = messageRepository.save(messageEntity);
        savedMessageMono.subscribe(savedMessage -> {
            log.info("Saved message: {}", savedMessage);
        });
    }
    private String getRoomNumber(WebSocketSession session) {
        String uriPath = session.getHandshakeInfo().getUri().getPath();
        String[] pathSegments = uriPath.split("/");
        return pathSegments[pathSegments.length - 1];
    }

    private MultiValueMap<String, String> parseQueryParams(String query) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (query != null) {
            Arrays.stream(query.split("&"))
                    .map(param -> param.split("="))
                    .forEach(param -> {
                        String name = UriUtils.decode(param[0], StandardCharsets.UTF_8);
                        String value = (param.length > 1) ? UriUtils.decode(param[1], StandardCharsets.UTF_8) : null;
                        queryParams.add(name, value);
                    });
        }
        return queryParams;
    }
}
