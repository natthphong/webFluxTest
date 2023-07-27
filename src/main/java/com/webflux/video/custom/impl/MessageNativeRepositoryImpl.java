package com.webflux.video.custom.impl;

import com.webflux.video.MessageEntity;
import com.webflux.video.custom.MessageNativeRepository;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MessageNativeRepositoryImpl implements MessageNativeRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final DatabaseClient databaseClient;

    public Flux<MessageEntity> findAllAsSlice(final Query query, final Pageable pageable) {
        final Query q = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .sort(pageable.getSort());
        return r2dbcEntityTemplate.select(q, MessageEntity.class);
    }
    @Override
    public Mono<Page<MessageEntity>> findAllAsPage(Query query, Pageable pageable) {
        final Mono<Long> count = r2dbcEntityTemplate.count(query, MessageEntity.class);
        final Flux<MessageEntity> slice = findAllAsSlice(query, pageable);
        return Mono.zip(count, slice.buffer().next().defaultIfEmpty(Collections.emptyList()))
                .map(output -> new PageImpl<>(
                        output.getT2(),
                        pageable,
                        output.getT1()
                ));
    }

    @Override
    public Mono<MessageEntity> findById(final String id) {
        return databaseClient.sql("SELECT * FROM tbl_message WHERE id = :id")
                .bind("id", Long.valueOf(id)) // Bind the id as a String
                .fetch()
                .one()
                .map(this::mapRowToMessageEntity);
    }

    @Override
    public Mono<List<MessageEntity>> findAll() {
        return databaseClient.sql("SELECT * FROM tbl_message").fetch()
                .all().map(this::mapRowToMessageEntity).collectList();

    }

    private MessageEntity mapRowToMessageEntity(Map<String, Object> map) {
        MessageEntity message = new MessageEntity();
//        message.setId((String) map.get("id"));
        message.setRoomNumber((String) map.get("room_number"));
        message.setContent((String) map.get("content"));
        message.setSessionId((String) map.get("session_id"));
        message.setUsername((String) map.get("username"));
        // Add mappings for other properties if needed
        return message;
    }

}
