package com.example.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
// Must be a static class, will not supply bean otherwise
public class TestConfig {
	
	// Must name this bean differently if the same bean already exists in another configuration
    @Bean(name = "testPerson")
    // @Primary - Use @Qualifier(name) in your client bean instead of depending on @Primary
    public Person getPerson() {
    	Person noob = new Person();
    	noob.setEmail("ralphfernandes@abc.com");
    	noob.setId("1");
    	noob.setName("Ralph Fernan");
    	return noob;
    }
}