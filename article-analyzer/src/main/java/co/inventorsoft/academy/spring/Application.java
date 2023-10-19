package co.inventorsoft.academy.spring;

import co.inventorsoft.academy.spring.services.AnalyzerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EntityScan("co.inventorsoft.academy.spring.models")
public class Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		AnalyzerService analyzerService = context.getBean(AnalyzerService.class);
		analyzerService.analyze();


	}

}
