package com.beneboba.spring_postgres;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

//@SpringBootApplication
//public class SpringPostgresApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(SpringPostgresApplication.class, args);
//	}
//}

@SpringBootApplication
public class SpringPostgresApplication implements ApplicationRunner, CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringPostgresApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("Application started from ApplicationRunner with option names : " +
				args.getOptionNames());
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Application started from CommandLineRUnner with args : "+ Arrays.toString(args));

	}
}