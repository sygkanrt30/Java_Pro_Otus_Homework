package ru.otus.homework.servlet;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import lombok.AllArgsConstructor;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.services.UserAuthService;

@SuppressWarnings({"java:S1989"})
@AllArgsConstructor
public class LoginServlet extends HttpServlet {
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private final transient TemplateProcessor templateProcessor;
    private final transient UserAuthService userAuthService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/clients");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
