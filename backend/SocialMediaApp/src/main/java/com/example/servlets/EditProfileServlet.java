package com.example.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.User;
import org.example.service.UserService;

import javax.servlet.ServletException;
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

@WebServlet("/api/editProfile") // Updated endpoint for API
public class EditProfileServlet extends HttpServlet {
    private UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            User user = userService.getUser(userId);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(user));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not retrieve user profile.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }
        int userId = (int) session.getAttribute("userId");
        String firstName ="";
        String lastName = "";
        String bio = "";
        String email ="";
        String password ="";
        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());
             firstName = jsonNode.has("firstname") ? jsonNode.get("firstname").asText() : "";
             lastName = jsonNode.has("lastname") ? jsonNode.get("lastname").asText() : "";
             bio = jsonNode.has("bio") ? jsonNode.get("bio").asText() : "";
             email = jsonNode.has("email") ? jsonNode.get("email").asText() : "";
             password = jsonNode.has("password") ? jsonNode.get("password").asText():"";

        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid JSON request body.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
            return; 
        }

      

        if (password != null && !password.equals("")) {
		    userService.updatePassword(userId, password);
		}

		User user = new User.Builder().firstName(firstName).lastName(lastName).bio(bio).email(email).build();
		userService.update(userId, user);

		Map<String, String> successResponse = new HashMap<>();
		successResponse.put("message", "Profile updated successfully.");
		resp.setContentType("application/json");
		resp.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }
}