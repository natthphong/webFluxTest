//package com.webflux.video.message;
//
//import jdk.jfr.Event;
//import reactor.core.publisher.UnicastProcessor;
//
//import java.util.Optional;
//
//public class WebSocketSubscriber {
//    private UnicastProcessor<Event> eventPublisher;
//    private Optional<Event> lastReceivedEvent = Optional.empty();
//
//    public WebSocketSubscriber(UnicastProcessor<Event> eventPublisher) {
//        this.eventPublisher = eventPublisher;
//    }
//
//    public void onNext(Event event) {
//        lastReceivedEvent = Optional.of(event);
//        eventPublisher.onNext(event);
//    }
//
//    public void onError(Throwable error) {
//        //TODO log error
//        error.printStackTrace();
//    }
//
//    public void onComplete() {
//        lastReceivedEvent.ifPresent(event -> eventPublisher.onNext(
//                Event.type(USER_LEFT)
//                        .withPayload()
//                        .user(event.getUser())
//                        .build()));
//    }
//}
