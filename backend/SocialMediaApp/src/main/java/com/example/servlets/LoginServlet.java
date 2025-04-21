package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Users.UsersDaoImpl.DataIntegrityException;
import org.example.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login") 
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.print(username+" "+password);

        try {
            boolean isValid = userService.login(username, password);

            if (isValid) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                int userId = userService.getUser(username).getUserId();
                session.setAttribute("userId", userId);
                session.setMaxInactiveInterval(60 * 60 * 24); // 24 hours

                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("message", "Login successful.");
                successResponse.put("userId", userId);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(successResponse));

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid username or password.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        } catch (SQLException | DataIntegrityException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}