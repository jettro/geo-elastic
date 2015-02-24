package nl.gridshore.geoelastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main class to start working with spring.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class Application {

    @Autowired
    SearchService searchService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/")
    String home() {

        return "Hello World! : " + searchService.numDocs() + ", " + searchService.doSomething();
    }
}
