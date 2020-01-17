package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

import javax.annotation.Resource;

import org.elasticsearch.client.RestHighLevelClient;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
public class DemoPropsIT extends AbstractTestNGSpringContextTests {
	
	@Value("${ES_PORT}")
	int port;
	
	@Autowired
	//@Qualifier("elasticsearchClient")
	RestHighLevelClient elasticsearchClient;
	
	@Test
	void testPortConfig() {
		assertThat(port).isEqualTo(31920);
	}

	@Test
	void testRestConfig() {
		assertThatObject(elasticsearchClient).isNotNull();
		int port = elasticsearchClient.getLowLevelClient().getNodes().get(0).getHost().getPort();
		assertThat(port).isEqualTo(31920);
	}

}
