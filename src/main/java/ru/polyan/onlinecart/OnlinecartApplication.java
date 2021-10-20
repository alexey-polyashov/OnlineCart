package ru.polyan.onlinecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAspectJAutoProxy
@PropertySource("classpath:secret.properties")
public class OnlinecartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinecartApplication.class, args);

	}

}
