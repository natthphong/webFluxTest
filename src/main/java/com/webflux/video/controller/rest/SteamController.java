package com.webflux.video.controller.rest;


import com.webflux.video.MessageEntity;
import com.webflux.video.custom.MessageNativeRepository;
import com.webflux.video.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/app/api")
@Slf4j
public class SteamController {
    private final SteamService steamService;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageNativeRepository messageNativeRepository;

    public SteamController(SteamService steamService) {
        this.steamService = steamService;
    }

    @GetMapping(value = "/video/{videoId}" , produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable String videoId, @RequestHeader("Range") String range) {
        log.info("videoId {}" , videoId);
        log.info("range {}" , range);
        return steamService.getVideo();
    }

    @GetMapping(value = "/list/message"  , produces = "application/json")
    public Mono<List<MessageEntity>> getMessage(){
        Flux<MessageEntity> flux = messageRepository.findAll();
        return flux.collectList();
    }

    @GetMapping(value = "/page/message"  , produces = "application/json")
    public   Mono<Page<MessageEntity>> getPageMessage(@RequestParam(required = false,defaultValue = "0") Integer page ,
                                                      @RequestParam(required = false,defaultValue = "10") Integer size
//                                                      ,@RequestParam(required = false,defaultValue = "") Sort sort
                                                      ){
        final Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Order.by("createDate").with(Sort.Direction.DESC))
        );
        return messageNativeRepository.findAllAsPage(Query.empty(),pageable);
    }

    @GetMapping("/message/{id}")
    public Mono<MessageEntity> getMessageById(@PathVariable String id) {
        log.info("id {}" , id);
        return messageNativeRepository.findById(id);
    }

    @GetMapping("/message")
    public Mono<List<MessageEntity>> findAllNative() {
        return messageNativeRepository.findAll();
    }


}
