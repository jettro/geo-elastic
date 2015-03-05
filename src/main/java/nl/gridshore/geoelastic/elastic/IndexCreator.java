package nl.gridshore.geoelastic.elastic;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IndexCreator {
    private static final Logger logger = LoggerFactory.getLogger(IndexCreator.class);
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private String index;
    private Client client;

    private String settings;
    private Map<String, String> mappings;

    private IndexCreator(Client client, String index) {
        this.client = client;
        this.index = index;
    }

    /* API Methods */

    /**
     * Initialize the index builder using the elasticsearch client and the name of the index or alias.
     *
     * @param client Client with connection to elasticsearch cluster
     * @param index  String containing the name for the index or the alias
     * @return the created IndexCreator
     */
    public static IndexCreator build(Client client, String index) {
        logger.debug("IndexCreator for index {} is started", index);
        if (!StringUtils.hasLength(index)) {
            throw new IllegalArgumentException("Provide at least an index");
        }
        return new IndexCreator(client, index);
    }

    public void execute() {
        executeIndexCreation();
    }
    
    /* Fluent interface setter methods */

    /**
     * By providing the settings you indicate you do not want the settings from an original index. If you are creating
     * a new index, this is mandatory.
     *
     * @param settings String containing the source for the elasticsearch index settings
     * @return part of fluent interface
     */
    public IndexCreator settings(String settings) {
        this.settings = settings;
        return this;
    }

    /**
     * Add a mapping for specified type. By specifying a mapping you indicate you do not want to copy mappings from
     * an existing index. If you are creating a new index, this is mandatory.
     *
     * @param type    String containing the type to add a mapping for
     * @param mapping String containing the actual mapping for the provided type
     * @return part of fluent interface
     */
    public IndexCreator addMapping(String type, String mapping) {
        if (this.mappings == null) {
            this.mappings = new HashMap<>();
        }
        this.mappings.put(type, mapping);
        return this;
    }

    private void executeIndexCreation() {
        client.admin().indices().prepareDelete(this.index + "*")
                .setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false)).get();

        String indexName = this.index + "-" + LocalDateTime.now().format(dateTimeFormatter);
        CreateIndexRequestBuilder indexBuilder = client.admin().indices().prepareCreate(indexName);
        indexBuilder.setSettings(this.settings);
        mappings.forEach(indexBuilder::addMapping);

        indexBuilder.execute().actionGet();
        IndicesAliasesRequestBuilder indicesAliasesRequestBuilder = client.admin().indices().prepareAliases();
        indicesAliasesRequestBuilder.addAlias(indexName, index).get();
    }

}
