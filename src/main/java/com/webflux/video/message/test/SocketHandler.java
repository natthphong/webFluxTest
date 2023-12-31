//package com.webflux.video.message.test;
//
//import lombok.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.BinaryMessage;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//public class SocketHandler extends TextWebSocketHandler {
//
//    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//    @Override
//    protected void handleTextMessage(@NonNull WebSocketSession session,@NonNull TextMessage message) {
//        for (WebSocketSession webSocketSession:sessions){
//            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())){
//                try {
//                    webSocketSession.sendMessage(message);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//}
