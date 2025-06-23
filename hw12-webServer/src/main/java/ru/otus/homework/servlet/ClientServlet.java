package ru.otus.homework.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.hibernate.crm.model.Address;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.model.Phone;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;
import ru.otus.homework.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class ClientServlet extends HttpServlet {
    public static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient dbServiceClient;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        List<Client> clients = dbServiceClient.findAll();
        List<Converter.JsonClient> jsonClients = Converter.convertToClientsForSerializable(clients);
        log.info("Client phoneList: {}", jsonClients);
        Map<String, Object> templateData = Map.of("clients", jsonClients);
        generatePage(resp, templateData);
    }

    private void generatePage(HttpServletResponse resp, Map<String, Object> templateData) {
        try {
            String page = templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, templateData);
            resp.getWriter().println(page);
        } catch (IOException e) {
            log.error("Error processing request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
