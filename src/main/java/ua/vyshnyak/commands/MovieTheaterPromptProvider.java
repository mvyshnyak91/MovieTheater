package ua.vyshnyak.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MovieTheaterPromptProvider implements PromptProvider {

    @Override
    public String getPrompt() {
        return "movie-theater-shell> ";
    }

    @Override
    public String getProviderName() {
        return "Movie Theater Prompt";
    }
}
