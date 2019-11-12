package com.pttl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(value = "com.pttl")
@EnableScheduling
@EnableAspectJAutoProxy
public class CompensateApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompensateApplication.class, args);
	}

}
