package indix.datamonster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import indix.datamonster.handlers.DataProcessor;

/**
 * @author prasad
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
//		DataProcessor.process();
	}
}
