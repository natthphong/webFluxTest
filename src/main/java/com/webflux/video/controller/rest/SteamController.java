package com.webflux.video.controller.rest;


import com.webflux.video.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/app/api")
@Slf4j
public class SteamController {
    private final SteamService steamService;

    public SteamController(SteamService steamService) {
        this.steamService = steamService;
    }

    @GetMapping(value = "/video" , produces = "video/mp4")
    public Mono<Resource> getVideo(@RequestHeader("Range") String range) {
        log.info("range {}" , range);
        return steamService.getVideo();
    }

}
