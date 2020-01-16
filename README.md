SpringBoot TestConfiguration Example
# Rules:
1. @SpringBootTest to create the ApplicationContext used in tests.
2. Use a nested @TestConfiguration class to customize the primary configuration.
3. Alternatively, define a top-level @TestConfiguration class and @Import it in your test.
4. Overriding beans are no longer allowed.  Assign a bean name via @Bean, and use @Qualifier to reference it during autowiring.