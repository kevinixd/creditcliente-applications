package com.kevincruz.chn.prestamosbancarios.creditcliente_applications;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CreditclienteApplicationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditclienteApplicationsApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
