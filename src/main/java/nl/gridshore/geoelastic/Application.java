package nl.gridshore.geoelastic;

import nl.gridshore.geoelastic.postalcode.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    ImportService importService;

    @Autowired
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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

    @RequestMapping("/percolator/province")
    Province obtainProvince(@RequestParam("province") String province) {
        return searchService.obtainPercolatedProvince(province);
    }

    @RequestMapping("/postalcode/import")
    String importPostalCodes() {
        importService.importPostalCodes(storageService::storePostalCode);
        return "postal codes imported";
    }

    @RequestMapping("/postalcode/count")
    long countPostalCodes() {
        return searchService.numberOfPostalCodes();
    }
}
