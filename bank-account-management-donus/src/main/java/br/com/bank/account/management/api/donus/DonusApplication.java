package br.com.bank.account.management.api.donus;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "br.com.bank.account.management.api.donus")
@EntityScan(basePackages = "br.com.bank.account.management.api.donus.model")
@EnableAutoConfiguration
public class DonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonusApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
