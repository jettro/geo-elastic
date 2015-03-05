package nl.gridshore.geoelastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.geoelastic.elastic.IndexCreator;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.action.percolate.PercolateSourceBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonFilterBuilder;
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

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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

    public long numDocs() {

        return client.prepareCount("gridshore-*").get().getCount();
    }

    public long doSomething() {
        String provincePoints = "noordholland.txt";
        FilteredQueryBuilder filteredQueryBuilder = createQuery(provincePoints);

        return client.prepareSearch("gridshore-*").setQuery(filteredQueryBuilder).get().getHits().getTotalHits();
    }

    public String checkLocationForProvince(double longitude, double latitude) {
        try {
            XContentBuilder docToCheck = jsonBuilder()
                    .startObject()
                    .startObject("geoip")
                    .startObject("location")
                    .field("lat", latitude)
                    .field("lon", longitude)
                    .endObject()
                    .endObject()
                    .endObject();
            System.out.println(docToCheck.string());
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

    private FilteredQueryBuilder createQuery(String provincePoints) {
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

        GeoPolygonFilterBuilder geoPolygonFilterBuilder = FilterBuilders.geoPolygonFilter("geoip.location");

        polygon.stream().forEach(geoPolygonFilterBuilder::addPoint);

        return filteredQuery(matchAllQuery(), geoPolygonFilterBuilder);
    }
}
