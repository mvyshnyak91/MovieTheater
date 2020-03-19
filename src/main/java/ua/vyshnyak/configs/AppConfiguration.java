package ua.vyshnyak.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "ua.vyshnyak.services",
        "ua.vyshnyak.repository"
})
@Import(value = {
        AuditoriumsConfig.class,
        AspectsConfig.class
})
public class AppConfiguration {
}
