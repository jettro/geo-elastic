package nl.gridshore.geoelastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.geoelastic.elastic.IndexCreator;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.action.percolate.PercolateSourceBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Search service to expose search functionality
 */
@Service
public class SearchService {
    public static final String GEO_INDEX = "geostuff";
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private Client client;

    @Autowired
    public SearchService(Client client) {
        this.client = client;
    }

    public long numberOfPostalCodes() {
        return client.prepareSearch(GEO_INDEX).setTypes("locations").setSize(0)
                .get().getHits().getTotalHits();
    }

    public long doSomething() {
        String provincePoints = "noordholland.txt";
        QueryBuilder filteredQueryBuilder = createQuery(provincePoints);

        return client.prepareSearch("geostuff").setQuery(filteredQueryBuilder).get().getHits().getTotalHits();
    }

    public String checkLocationForProvince(double longitude, double latitude) {
        try {
            XContentBuilder docToCheck = jsonBuilder()
                    .startObject()
                    .startObject("location")
                    .field("lat", latitude)
                    .field("lon", longitude)
                    .endObject()
                    .endObject();
            PercolateSourceBuilder.DocBuilder builder = new PercolateSourceBuilder.DocBuilder();
            builder.setDoc(docToCheck);

            PercolateResponse matches = client.preparePercolate()
                    .setPercolateDoc(builder)
                    .setIndices("geostuff")
                    .setDocumentType("locations")
                    .get();
            if (matches.getMatches().length > 0) {
                return matches.getMatches()[0].getId().toString();
            }
            return "Not in a province";
        } catch (IOException e) {
            logger.error("Problem while checking a provided point for a provence");
            return e.getMessage();
        }
    }

    public void createPercolateQueries() {
        try {
            IndexCreator.build(this.client, GEO_INDEX)
                    .settings(StreamUtils.copyToString(new ClassPathResource(GEO_INDEX + "-settings.json").getInputStream(), Charset.defaultCharset()))
                    .addMapping("locations", StreamUtils.copyToString(new ClassPathResource(GEO_INDEX + "-mapping.json").getInputStream(), Charset.defaultCharset()))
                    .execute();
            List<String> provinces = Arrays.asList("drenthe", "flevoland", "friesland", "gelderland", "groningen", "limburg", "noordbrabant", "noordholland", "overijssel", "utrecht", "zeeland", "zuidholland");
            provinces.stream().forEach(this::doCreatePercolatorQuery);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create the polyglot query");
        }
    }

    public Province obtainPercolatedProvince(String province) {
        GetResponse response = client.prepareGet(GEO_INDEX, ".percolator", province).setFetchSource("query.*", null).get();
        Map<String, Object> source = response.getSource();
        Map<String, Object> query = (Map<String, Object>) source.get("query");
        Map<String, Object> geoPolugon = (Map<String, Object>) query.get("geo_polygon");
        Map<String, Object> location = (Map<String, Object>) geoPolugon.get("location");
        ArrayList<ArrayList<Double>> points = (ArrayList<ArrayList<Double>>) location.get("points");

        return new Province(province, points);
    }

    private void doCreatePercolatorQuery(String province) {
        try {
            client.prepareIndex(GEO_INDEX, ".percolator", "province_" + province)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("query", createQuery(province + ".txt"))
                            .field("province", province)
                            .endObject()
                    ).setRefresh(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private QueryBuilder createQuery(String provincePoints) {
        final List<GeoPoint> polygon = new ArrayList<>();
        List<String> geo = null;
        try {
            geo = new ObjectMapper().readValue(new ClassPathResource(provincePoints).getInputStream(), List.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read province file with points " + provincePoints);
        }

        geo.stream().forEach(s -> {
            polygon.add(new GeoPoint(s));
        });

        GeoPolygonQueryBuilder geoPolygonFilterBuilder = QueryBuilders.geoPolygonQuery("location");

        polygon.stream().forEach(geoPolygonFilterBuilder::addPoint);

        return geoPolygonFilterBuilder;
    }
}
