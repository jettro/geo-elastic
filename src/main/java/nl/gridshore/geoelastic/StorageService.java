package nl.gridshore.geoelastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.gridshore.geoelastic.postalcode.CSVPostalcode;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service that facilitates storing the PostalCode as imported from the csv to our elasticsearch index.
 */
@Service
public class StorageService {
    private ObjectMapper mapper;
    private Client client;

    @Autowired
    public StorageService(Client client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public void storePostalCode(CSVPostalcode CSVPostalcode) {

        PostalCode postalCode = PostalCode.from(CSVPostalcode);

        try {
            client.prepareIndex("geostuff", "locations")
                    .setSource(mapper.writeValueAsString(postalCode))
                    .get();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
