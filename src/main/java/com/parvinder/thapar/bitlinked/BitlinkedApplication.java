package com.parvinder.thapar.bitlinked;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.parvinder.thapar.bitlinked.controller.BitLinkedController;

@SpringBootApplication
@ComponentScan(basePackageClasses = BitLinkedController.class)
@EntityScan("com.parvinder.thapar.bitlinked.beans")
@EnableJpaRepositories("com.parvinder.thapar.bitlinked.jpa")
public class BitlinkedApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitlinkedApplication.class, args);
	}
}