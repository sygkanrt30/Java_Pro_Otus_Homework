package ru.otus.homework.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;

@AllArgsConstructor
@Slf4j
public class ClientApiServlet extends HttpServlet {
    private final transient Gson gson;
    private final DBServiceClient dbServiceClient;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Client> clients = dbServiceClient.findAll();
        resp.setContentType("application/json;charset=UTF-8");
        var json = gson.toJson(clients);
        printJsonToPage(resp, json);
    }

    private void printJsonToPage(HttpServletResponse resp, String gson) {
        try {
            resp.getWriter().print(gson);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String body = getBody(req);
        var client = gson.fromJson(body, Client.class);
        dbServiceClient.saveClient(client);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json;charset=UTF-8");
        var json = gson.toJson(client);
        printJsonToPage(resp, json);
    }

    private String getBody(HttpServletRequest req) {
        try {
            return req.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
