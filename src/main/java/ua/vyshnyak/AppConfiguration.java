package ua.vyshnyak;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "ua.vyshnyak.aspects",
        "ua.vyshnyak.services"
})
@Import(value = {
        AuditoriumsConfig.class,
        RepositoryConfigImportSelector.class
})
public class AppConfiguration {
}
