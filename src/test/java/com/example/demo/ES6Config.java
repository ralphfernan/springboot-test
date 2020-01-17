package com.example.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class ES6Config {
	
	@Bean(name="es6HostsWithPort")
    public String[] getes6HostsWithPort() {
    	final String PREFIX = "qa-mesos-es-0";
    	final int NUM_HOSTS = 2;
    	final int PORT = 31926;
    	String [] hosts = new String[NUM_HOSTS];
    	
    	for (int i=0; i<NUM_HOSTS; i++) {
    		hosts[i] = PREFIX + String.valueOf(i+1) + ":" + PORT;
    	}
    	
    	return hosts;	
    }
    
}
