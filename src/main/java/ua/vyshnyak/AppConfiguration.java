package ua.vyshnyak;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
@Import(AuditoriumsConfig.class)
public class AppConfiguration {
}
