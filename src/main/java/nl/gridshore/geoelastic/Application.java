package nl.gridshore.geoelastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

/**
 * Main class to start working with spring.
 *
 * http://localhost:8080/percolator/check?lat=52.060669000000000000&lon=4.494024999999965000
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

    @RequestMapping("/percolator/add")
    String addPercolator() {
        searchService.createPercolateQueries();
        return "Added the percolator queries";
    }

    @RequestMapping("/percolator/check")
    String checkPercolator(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return searchService.checkLocationForProvince(lon, lat);
    }

    @RequestMapping(value = "/city/add", method = RequestMethod.POST)
    String addCityForm(@RequestBody City city) {
        System.out.println(city.toString());
        return "city stored";
    }
}
