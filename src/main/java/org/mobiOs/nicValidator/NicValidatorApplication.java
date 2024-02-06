package org.mobiOs.nicValidator;

import org.mobiOs.nicValidator.controller.NICValidateController;
import org.mobiOs.nicValidator.service.UserService;
import org.mobiOs.nicValidator.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class NicValidatorApplication {

	@Autowired
	private UserServiceImpl userService;

	public static void main(String[] args) {
		SpringApplication.run(NicValidatorApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


	@EventListener(ApplicationReadyEvent.class)
	public void triggerMail(){}


}
