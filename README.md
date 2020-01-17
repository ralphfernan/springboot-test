SpringBoot TestConfiguration Example
# Configuration options
1. Using @TestConfiguration
2. Using application.properties

# Rules:
1. @SpringBootTest to create the ApplicationContext used in tests.
2. Use a nested @TestConfiguration class to customize the primary configuration.
3. Alternatively, define a top-level @TestConfiguration class and @Import it in your test.
4. @TestConfiguration classes are not picked up by scanning.
5. Overriding beans are no longer allowed.  Assign a bean name via @Bean, and use @Qualifier to reference it during autowiring.
6. Tests will pick up props from src/test/resources/application.properties if present.  Otherwise src/main/resources/application.properties