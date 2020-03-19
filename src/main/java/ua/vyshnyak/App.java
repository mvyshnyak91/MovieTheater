package ua.vyshnyak;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.vyshnyak.configs.AppConfiguration;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        System.out.println("hello");
    }
}
