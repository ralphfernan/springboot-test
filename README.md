SpringBoot TestConfiguration Example
# Branches/Configuration Options
1. Using @TestConfiguration - see master
2. Using application.properties - see branch spring-elasticsearch-config-options
3. Note: other configuration options not covered, ex. spring-data-elasticsearch built-in properties.
    

# Rules:
1. @SpringBootTest to create the ApplicationContext used in tests.
2. Use a nested @TestConfiguration class to customize the primary configuration.
3. Alternatively, define a top-level @TestConfiguration class and @Import it in your test.
4. Overriding beans are no longer allowed.  Assign a bean name via @Bean, and use @Qualifier to reference it during autowiring.