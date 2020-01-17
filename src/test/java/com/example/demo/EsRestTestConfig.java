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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
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


@TestConfiguration
@Import(ES6Config.class) // Configuration composition
@EnableElasticsearchRepositories(basePackages = "com.example.demo")
public class EsRestTestConfig {

    private static final Logger LOG = LogManager.getLogger(EsRestConfig.class.toString());

	private static final String ES2_HOSTS_PREFIX = "qa-mesos-private-0";
	private static final String ES6_HOSTS_PREFIX = "qa-mesos-es-0";
    
    private String[] elasticSearchHostsWithPort = getElasticSearchHosts(ES6_HOSTS_PREFIX, 31926, 2);
    
    
    private final ES6Config es6config;
    
    
    private final String [] es6HostsWithPort;

    public static String[] getElasticSearchHosts(final String prefix, int port, int num_hosts) {
    	String [] hosts = new String[num_hosts];
    	
    	for (int i=0; i<num_hosts; i++) {
    		hosts[i] = prefix + String.valueOf(i+1) + ":" + port;
    	}
    	
    	return hosts;	
    }

    
    public EsRestTestConfig(final ES6Config esConfig) {
    	this.es6config = esConfig;
    	this.es6HostsWithPort = esConfig.getes6HostsWithPort();
	}
    /*
     * Thread-safe
     */
    @Bean(name = "testElasticsearchClient")
    public RestHighLevelClient elasticsearchClient() {
    	ClientConfiguration restConfig = ClientConfiguration.builder().connectedTo(es6HostsWithPort).build();
        LOG.info("Connecting to elastic search on host-ports: {}", Strings.join(elasticSearchHostsWithPort, ", "));
		
        return RestClients.create(restConfig).rest();
    }

    

    
}


