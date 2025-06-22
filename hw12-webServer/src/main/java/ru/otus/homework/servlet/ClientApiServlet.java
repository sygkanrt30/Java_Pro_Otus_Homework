package ru.otus.homework.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.hibernate.crm.model.Address;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.model.Phone;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        try {
            String name = req.getParameter("name");
            String address = req.getParameter("address");
            String phone = req.getParameter("phone");

            Client client = new Client(null,
                    name,
                    new Address(address),
                    new ArrayList<>(List.of(new Phone(phone))));
            dbServiceClient.saveClient(client);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print(gson.toJson(client));
        } catch (Exception e) {
            log.error("Error creating client", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
