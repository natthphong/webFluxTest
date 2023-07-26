package com.webflux.video.controller.rest;

import com.webflux.video.MessageEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface MessageRepository extends R2dbcRepository<MessageEntity, String> {

    Flux<MessageEntity> findByRoomNumberAndIsDeleted(String roomNumber,String isDeleted);
}
