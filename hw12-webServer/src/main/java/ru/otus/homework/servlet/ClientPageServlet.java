package ru.otus.homework.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.homework.hibernate.crm.model.Client;
import ru.otus.homework.hibernate.crm.service.DBServiceClient;
import ru.otus.homework.services.TemplateProcessor;

@AllArgsConstructor
public class ClientPageServlet extends HttpServlet {
    public static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient dbServiceClient;

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        // Получаем список клиентов из базы данных
        List<Client> clients = dbServiceClient.findAll();

        // Создаем данные для шаблона
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("clients", clients);

        // Генерируем страницу с данными
        String page = templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, templateData);
        resp.getWriter().println(page);
    }
}
