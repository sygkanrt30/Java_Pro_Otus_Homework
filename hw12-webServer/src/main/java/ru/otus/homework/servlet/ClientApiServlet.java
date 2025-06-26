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

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class ClientApiServlet extends HttpServlet {
    private final DBServiceClient dbServiceClient;

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

            resp.sendRedirect("/clients");
        } catch (Exception e) {
            log.error("Error creating client", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
