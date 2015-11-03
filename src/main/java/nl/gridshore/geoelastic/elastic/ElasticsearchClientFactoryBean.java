package nl.gridshore.geoelastic.elastic;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Factory bean creating the elasticsearch Client object
 */
@Component
public class ElasticsearchClientFactoryBean implements FactoryBean<Client>, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClientFactoryBean.class);

    ParseServerStringFunction parseServerConfigFunction = new ParseServerStringFunctionDefaultPort();

    @Value("#{'${elastic.unicast.hosts}'.split(',')}")
    private List<String> unicastHosts;

    @Value("${elastic.cluster.name}")
    private String clusterName;

    private Client client; //Thread safe: its lifecycle should be similar to the application lifecycle

    @Override
    public void destroy() {
        client.close();
    }

    @Override
    public Client getObject() {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return Client.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();

        logger.debug("Settings used for connection to elasticsearch : {}", settings.toDelimitedString('#'));

        List<TransportAddress> addresses = unicastHosts
                .stream()
                .map(parseServerConfigFunction::parse)
                .collect(toList());

        logger.debug("Hosts used for transport client : {}", addresses);

        client = TransportClient.builder().settings(settings).build()
                .addTransportAddresses(addresses.toArray(new TransportAddress[addresses.size()]));
    }

}
