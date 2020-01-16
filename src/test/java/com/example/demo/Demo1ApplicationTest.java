package com.example.demo;

import static org.assertj.core.api.Assertions.assertThatObject;

//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
@Import(TestConfig.class)
class Demo1ApplicationTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("testPerson")
	Person person;

	@Test
	void contextLoads() {
		assertThatObject(person).hasFieldOrPropertyWithValue("name", "Ralph Fernan");
	}
	
	/* Reference TestConfiguration class as top-level or inner static as below.
	 * To reference top-level, use @Import(TestConfiguration.class)

	@TestConfiguration
	// Must be a static class, will not supply bean otherwise
	public static class TestConfig {
		
		// Must name this bean differently if the same bean already exists in another configuration
		// Overriding beans is disabled by default since Boot 2.1.
		// To enable overriding beans, set spring.main.allow-bean-definition-overriding=true
		// This is not preferred, since the order of bean instantiation matters. With annotations, controlling order is difficult.
	    @Bean(name = "testPerson")
	    // @Primary Use @Qualifier in your client bean instead of depending on @Primary
	    public Person getPerson() {
	    	Person noob = new Person();
	    	noob.setEmail("ralphfernandes@abc.com");
	    	noob.setId("1");
	    	noob.setName("Ralph Fernan");
	    	return noob;
	    }
	}
		*/

}
