package com.webflux.video.custom;

import com.webflux.video.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;


public interface MessageNativeRepository {
    Mono<Page<MessageEntity>> findAllAsPage(final Query query, final Pageable pageable);
     Mono<MessageEntity> findById(final String id);

     Mono<List<MessageEntity>> findAll();
}
