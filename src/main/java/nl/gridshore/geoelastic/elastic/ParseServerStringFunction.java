package nl.gridshore.geoelastic.elastic;

import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Strategy for obtaining an {@link org.elasticsearch.common.transport.InetSocketTransportAddress} from a String containing server information
 */
@FunctionalInterface
public interface ParseServerStringFunction {
    public InetSocketTransportAddress parse(String serverConfig);
}
