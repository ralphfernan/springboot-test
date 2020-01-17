package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import joptsimple.internal.Strings;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.demo")
public class EsRestConfig extends AbstractElasticsearchConfiguration {

    private static final Logger LOG = LogManager.getLogger(EsRestConfig.class.toString());
    
    @Value("${ES_HOSTS_PREFIX}")
    private String ES_HOSTS_PREFIX;
    
    @Value("${ES_PORT}")
    private int ES_PORT;
	
    private String elasticsearchCluster = "es-cluster";
    
    public static String[] getElasticSearchHosts(final String prefix, int port, int num_hosts) {
    	String [] hosts = new String[num_hosts];
    	
    	for (int i=0; i<num_hosts; i++) {
    		hosts[i] = prefix + String.valueOf(i+1) + ":" + port;
    	}
    	
    	return hosts;	
    }

    /*
     * Thread-safe
     */
    @Bean
    public RestHighLevelClient elasticsearchClient() {
    	ClientConfiguration restConfig = ClientConfiguration.builder().connectedTo(getElasticSearchHosts(ES_HOSTS_PREFIX, ES_PORT, 2)).build();
        LOG.info("Connecting to elastic search on host-ports: {}", Strings.join(getElasticSearchHosts(ES_HOSTS_PREFIX, ES_PORT, 2), ", "));
		
        return RestClients.create(restConfig).rest();
    }

    @Bean
	@Override
	  public EntityMapper entityMapper() {                     
	    ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
	        new DefaultConversionService());
	    entityMapper.setConversions(elasticsearchCustomConversions());

	    return entityMapper;
	  }
    
    
}


