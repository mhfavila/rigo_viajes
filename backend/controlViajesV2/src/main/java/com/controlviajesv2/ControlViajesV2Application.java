package com.controlviajesv2;

import com.controlviajesv2.security.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ControlViajesV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ControlViajesV2Application.class, args);
	}

}
