package ru.polyan.onlinecart;

import javassist.expr.NewArray;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import ru.polyan.onlinecart.dto.NewOrderDto;
import ru.polyan.onlinecart.model.Order;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@SpringBootApplication
@EnableAspectJAutoProxy
@PropertySource("classpath:secret.properties")
public class OnlinecartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinecartApplication.class, args);

	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STANDARD)
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true)
				.setFieldAccessLevel(PRIVATE);
		return mapper;
	}

//TODO: добавить возможность отмены заказа (только для статуса принят, после оплаты отмена не возможна) 17.10

//TODO: проработать работу с базой данных, добавить графы там где возможно 18.10

//TODO: добавить страницу товара 20.10

//TODO: вынести контролер по загрузке картинок в отдельный модуль 23.10

//TODO: упаковать в докер 24.10

}
