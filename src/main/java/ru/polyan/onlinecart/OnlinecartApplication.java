package ru.polyan.onlinecart;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

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

//TODO: добавить оплату заказа не через PayPal

//TODO: подробнее описать контролеры в swagger


}
