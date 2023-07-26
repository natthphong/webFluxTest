package com.webflux.video.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@Service
public class SteamService {

    private final ResourceLoader resourceLoader;

    public SteamService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> getVideo(){

        try {
            return  Mono.fromSupplier(()-> resourceLoader.getResource("classpath:videos/test.mp4"));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
