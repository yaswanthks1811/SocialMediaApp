package com.example.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.MessageService;

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

@WebServlet("/api/sendMessage")
public class SendMessageServlet extends HttpServlet {

    private MessageService messageService = new MessageService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Integer senderId = (Integer) session.getAttribute("userId");

        if (senderId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());
            String receiverIdStr = jsonNode.get("receiverId").asText();
            String message = jsonNode.get("message").asText();
            System.out.println(receiverIdStr + " " + message);

            if (receiverIdStr == null || receiverIdStr.isEmpty() || message == null || message.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Missing parameters.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            int receiverId = Integer.parseInt(receiverIdStr);
            messageService.sendMessage(senderId, receiverId, message);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Message sent successfully.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(successResponse));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid JSON request body.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }
}