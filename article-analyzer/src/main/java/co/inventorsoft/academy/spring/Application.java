package co.inventorsoft.academy.spring;

import co.inventorsoft.academy.spring.services.AnalyzerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		AnalyzerService analyzerService = context.getBean(AnalyzerService.class);
		analyzerService.analyze();


	}

}
