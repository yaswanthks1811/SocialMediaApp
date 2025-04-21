package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.User;
import org.example.service.MessageService;
import org.example.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class MessageInboxServlet
 */
@WebServlet("/api/inbox")
public class MessageInboxServlet extends HttpServlet {

    private MessageService messageService = new MessageService();
    private UserService userService = new UserService(); // Initialize UserService
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            List<User> senderList = messageService.getSenders(userId);
            for(User user:senderList){
            	System.out.print(user.getUsername());
            }
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(senderList));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to fetch Messages.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }

    }
}