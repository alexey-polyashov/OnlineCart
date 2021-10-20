package ru.polyan.onlinecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class OnlinecartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinecartApplication.class, args);

	}

}
