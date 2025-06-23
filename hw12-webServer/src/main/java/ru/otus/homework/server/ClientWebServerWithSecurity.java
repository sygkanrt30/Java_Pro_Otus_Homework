package ru.otus.homework.server;

import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.services.UserAuthService;
import ru.otus.homework.servlet.AuthorizationFilter;
import ru.otus.homework.servlet.LoginServlet;

import java.util.Arrays;

public class ClientWebServerWithSecurity extends ClientsWebServerBased {
    private final UserAuthService userAuthService;

    public ClientWebServerWithSecurity(
            int port,
            UserAuthService userAuthService,
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient) {
        super(port, templateProcessor, dbServiceClient);
        this.userAuthService = userAuthService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(
                new ServletHolder(new LoginServlet(templateProcessor, userAuthService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
