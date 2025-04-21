package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.User;
import org.example.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {

    private UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getParameter("query");
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        Integer currentUserId = (Integer) session.getAttribute("userId");
        List<User> results = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            results = userService.searchUsersByRegex(query, currentUserId);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(results));
    }
}