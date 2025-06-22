package ru.otus.homework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import ru.otus.homework.hibernate.core.repository.DataTemplateHibernate;
import ru.otus.homework.hibernate.core.repository.HibernateUtils;
import ru.otus.homework.hibernate.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.homework.hibernate.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.homework.hibernate.crm.model.Address;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.model.Phone;
import ru.otus.homework.hibernate.crm.model.User;
import ru.otus.homework.hibernate.crm.service.DbServiceClientImpl;
import ru.otus.homework.hibernate.crm.service.DbServiceUserImpl;
import ru.otus.homework.server.ClientWebServerWithSecurity;
import ru.otus.homework.server.ClientsWebServer;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.services.TemplateProcessorImpl;
import ru.otus.homework.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080/login

    // Страница клиентов
    http://localhost:8080/clients

    // REST сервис
    http://localhost:8080/api/clients
*/
@Slf4j
public class WebServer {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, User.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var userTemplate = new DataTemplateHibernate<>(User.class);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceUser = new DbServiceUserImpl(userTemplate, transactionManager);
        var dbServiceClient = new DbServiceClientImpl(clientTemplate, transactionManager);
        var userAuthService = new UserAuthServiceImpl(dbServiceUser);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ClientsWebServer usersWebServer = new ClientWebServerWithSecurity(
                WEB_SERVER_PORT, userAuthService, gson, templateProcessor, dbServiceClient);

        startServer(usersWebServer);
    }

    private static void startServer(ClientsWebServer usersWebServer) {
        try {
            usersWebServer.start();
            usersWebServer.join();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
