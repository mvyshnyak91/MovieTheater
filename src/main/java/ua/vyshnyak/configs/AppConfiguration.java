package ua.vyshnyak.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ua.vyshnyak.RepositoryConfigImportSelector;

@Configuration
@ComponentScan(basePackages = {
        "ua.vyshnyak.services"
})
@Import(value = {
        AuditoriumsConfig.class,
        AspectsConfig.class,
        RepositoryConfigImportSelector.class
})
public class AppConfiguration {
}
