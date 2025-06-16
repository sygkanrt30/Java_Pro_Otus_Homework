package ru.otus.homework.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.homework.helpers.FileSystemHelper;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.servlet.ClientApiServlet;
import ru.otus.homework.servlet.ClientPageServlet;

public class ClientsWebServerBased implements ClientsWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final DBServiceClient dbServiceClient;

    public ClientsWebServerBased(
            int port, Gson gson, TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/clients", "/api/clients"));

        server.setHandler(sequence);
    }

    @SuppressWarnings({"squid:S1172"})
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
                new ServletHolder(new ClientApiServlet(gson, dbServiceClient)), "/api/clients");
        servletContextHandler.addServlet(
                new ServletHolder(new ClientPageServlet(templateProcessor, dbServiceClient)), "/clients");
        return servletContextHandler;
    }
}
