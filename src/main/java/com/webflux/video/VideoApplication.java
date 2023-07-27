package com.webflux.video;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.ConnectionAccessor;
import org.springframework.r2dbc.core.DatabaseClient;

@SpringBootApplication
@Configuration
public class VideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoApplication.class, args);
	}
	@Bean
	public R2dbcEntityTemplate r2dbcEntityTemplate(final ConnectionFactory connectionFactory){
		return new R2dbcEntityTemplate(connectionFactory);
	}

}
