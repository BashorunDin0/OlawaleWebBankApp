package com.wale.tjabank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
			title = "The Olawale Bank App",
			description = "Backend APIs for Olawale Bank",
			version = "v1.0",
			contact = @Contact(
					name = "Ibrahim Olawale",
					email = "ibrahimolawale02@gmail.com",
					url = "https://github.com/BashorunDin0"
			),
			license = @License(
					name = "The Olawale Dev Course",
					url = "https://github.com/BashorunDin0"
			)

	),
		externalDocs = @ExternalDocumentation(
				description = "The Olawale Bank App Documentation",
				url = "https://github.com/BashorunDin0"
		)
)
public class TjabankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TjabankApplication.class, args);
	}

}
