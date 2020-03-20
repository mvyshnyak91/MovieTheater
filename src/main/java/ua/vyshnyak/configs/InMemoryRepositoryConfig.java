package ua.vyshnyak.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ua.vyshnyak.repository.impl.inmemory")
public class InMemoryRepositoryConfig {
}
