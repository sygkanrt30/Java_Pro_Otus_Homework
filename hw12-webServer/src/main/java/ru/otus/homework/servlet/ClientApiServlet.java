package ru.otus.homework.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;

@AllArgsConstructor
public class ClientApiServlet extends HttpServlet {
    private final transient Gson gson;
    private final DBServiceClient dbServiceClient;

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Client> clients = dbServiceClient.findAll();
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(clients));
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String body = req.getReader().lines().collect(Collectors.joining());
        var client = gson.fromJson(body, Client.class);
        dbServiceClient.saveClient(client);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(client));
    }
}
