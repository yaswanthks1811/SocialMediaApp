package com.example.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Users.UsersDaoImpl.DataIntegrityException;
import org.example.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String username="";
    	String password="";
    	String email="";
        
        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());
             username = jsonNode.get("username").asText();
             password = jsonNode.get("password").asText();
             email = jsonNode.get("email").asText();
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid JSON request body.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }

        try {
            boolean success = userService.registerUser(username, password, email);

            if (success) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Registration successful.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(successResponse));
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username or Email already exists.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        } catch (SQLException | DataIntegrityException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}