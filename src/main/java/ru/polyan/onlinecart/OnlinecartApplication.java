package ru.polyan.onlinecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAspectJAutoProxy
@PropertySource("classpath:secret.properties")
public class OnlinecartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinecartApplication.class, args);

	}

//TODO: добавить SWAGER 16.10

//TODO: при оформлении заказа сделать переход на страницу заказа для возможности оплаты 17.10

//TODO: добавить возможность отмены заказа (только для статуса принят, после оплаты отмена не возможна) 17.10

//TODO: проработать работу с базой данных, добавить графы там где возможно 18.10

//TODO: добавить страницу товара 20.10

//TODO: немного поработать над дизайном
// выбрать цветовое решение 21.10

//TODO: вынести контролер по загрузке картинок в отдельный модуль 23.10

//TODO: упаковать в докер 24.10

}
