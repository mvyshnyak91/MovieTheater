package ua.vyshnyak.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MovieTheaterBannerProvider implements BannerProvider {

    public String getBanner() {
        StringBuffer buf = new StringBuffer();
        buf.append("=======================================")
                .append(OsUtils.LINE_SEPARATOR);
        buf.append("          Movie Theater Shell          ")
                .append(OsUtils.LINE_SEPARATOR);
        buf.append("=======================================")
                .append(OsUtils.LINE_SEPARATOR);
        buf.append("Version:")
                .append(this.getVersion());
        return buf.toString();
    }

    public String getVersion() {
        return "1.0";
    }

    public String getWelcomeMessage() {
        return "Welcome to Movie Theater CLI";
    }

    public String getProviderName() {
        return "Movie Theater Banner";
    }
}
