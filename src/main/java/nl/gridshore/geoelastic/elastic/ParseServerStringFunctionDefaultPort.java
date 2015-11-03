package nl.gridshore.geoelastic.elastic;

import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Strategy for obtaining an {@link org.elasticsearch.common.transport.InetSocketTransportAddress} from a String containing only a server name or a
 * combination of servername and port in the format of servername:port.
 * <p/>
 * If no port is provide in the serverString, we use the default port {@value #DEFAULT_ELASTICSEARCH_PORT}
 */
public class ParseServerStringFunctionDefaultPort implements ParseServerStringFunction {
    static final int DEFAULT_ELASTICSEARCH_PORT = 9300;
    private static final Logger logger = LoggerFactory.getLogger(ParseServerStringFunctionDefaultPort.class);

    @Override
    public InetSocketTransportAddress parse(String serverConfig) {
        int port = DEFAULT_ELASTICSEARCH_PORT;
        String serverName = serverConfig;
        if (serverConfig.contains(":")) {
            String[] splitted = serverConfig.split(":");
            serverName = splitted[0];
            port = Integer.parseInt(splitted[1].trim());
        }
        try {
            return new InetSocketTransportAddress(InetAddress.getByName(serverName), port);
        } catch (UnknownHostException e) {
            logger.error("Cannot connect to an elasticsearch cluster based on a wrong host config", e);
            throw new HostConfigException(serverName);
        }
    }
}
