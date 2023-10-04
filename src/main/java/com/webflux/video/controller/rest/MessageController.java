package com.webflux.video.controller.rest;

import com.webflux.video.MessageEntity;
import io.r2dbc.postgresql.api.Notification;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@RestController
//@EnableR2dbcRepositories(considerNestedRepositories = true)
public class MessageController {

    private final MessageRepository messageRepository;


    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }



    @GetMapping(value = "/chat/id/{roomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MessageEntity>> getMessages(@PathVariable(name = "roomId") String roomId) {
        Flux<MessageEntity> messageFlux = messageRepository.findByRoomNumberAndIsDeleted(roomId, "N");
        return messageFlux.map(message -> ServerSentEvent.<MessageEntity> builder()
                        .id(UUID.randomUUID().toString())
                        .event("message-event")
                        .data(message)
                        .build())
                .subscribeOn(Schedulers.boundedElastic());
    }

//    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<CharSequence> getStream() {
//        return connection.getNotifications().map(Notification::getParameter);
//    }
    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now())
                        .build());
    }

    @PostMapping("/chat/id/{roomId}")
    public Mono<MessageEntity> newMessage(@PathVariable(name = "roomId") String roomId,@RequestBody Map<String, Object> body) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setRoomNumber(roomId);
        messageEntity.setIsDeleted("N");
        messageEntity.setCreateDate(LocalDateTime.now());
        messageEntity.setContent((String) body.get("message"));
        return messageRepository.save(messageEntity).then(Mono.fromRunnable(() -> System.out.println("save success")));
    }
}
