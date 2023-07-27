package com.webflux.video;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webflux.video.controller.rest.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Exceptions;
import reactor.core.Fuseable;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@SpringBootTest
class VideoApplicationTests {

	@Test
	void contextLoads() {
		List<String> strings = null;

		if (isNullOrEmpty(strings)){
			System.out.println("hello");
		}

		new ArrayList<>();
	}

	public static <T> boolean isNullOrEmpty(List<T> array) {
		return array == null || array.isEmpty();
	}




}


@Slf4j
abstract class testType<T>{


	public final Mono<List<T>> collectList() {
		if (this instanceof Callable) {
			if (this instanceof Fuseable.ScalarCallable) {
				Fuseable.ScalarCallable<T> scalarCallable = (Fuseable.ScalarCallable) this;
				Object v;
				try {
					v = scalarCallable.call();
				} catch (Exception var4) {
					return Mono.error(Exceptions.unwrap(var4));
				}
			}

		}
		return null;
	}

	public final List<T> testList(){
		if (this instanceof MessageRepository){

		}
		return null;
	}

}