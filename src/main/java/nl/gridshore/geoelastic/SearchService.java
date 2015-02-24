package nl.gridshore.geoelastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonFilterBuilder;
import org.geojson.LngLatAlt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Search service to expose search functionality
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private Client client;

    @Autowired
    public SearchService(Client client) {
        this.client = client;
    }

    public long numDocs() {

        return client.prepareCount("gridshore-*").get().getCount();
    }

    public long doSomething() {

        try {
            final List<GeoPoint> polygon = new ArrayList<>();
            List<String> geo = new ObjectMapper().readValue(new ClassPathResource("noordholland.txt").getInputStream(), List.class);

            geo.stream().forEach(s -> {
                polygon.add(new GeoPoint(s));
            });

            GeoPolygonFilterBuilder geoPolygonFilterBuilder = FilterBuilders.geoPolygonFilter("geoip.location");

            polygon.stream().forEach(geoPolygonFilterBuilder::addPoint);

            FilteredQueryBuilder filteredQueryBuilder = filteredQuery(matchAllQuery(), geoPolygonFilterBuilder);

            return client.prepareSearch("gridshore-*").setQuery(filteredQueryBuilder).get().getHits().getTotalHits();
        } catch (IOException e) {
            logger.error("On no", e);
            return -1;
        }
    }

    private List<GeoPoint> getCoordinatesFromPolygon(List<List<LngLatAlt>> coordinatesPolygon) {
        List<GeoPoint> points = new ArrayList<>();
        for (List<LngLatAlt> lngLatAlts : coordinatesPolygon) {
            for (LngLatAlt lngLatAlt : lngLatAlts) {
                double longitude = lngLatAlt.getLongitude();
                double latitude = lngLatAlt.getLatitude();
                points.add(new GeoPoint(latitude, longitude));
            }
        }
        return points;
    }

}
