package com.example.servlets;

import com.example.DTO.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.Message;
import org.example.service.MessageService;
import org.example.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/viewMessages")
public class ViewMessagesServlet extends HttpServlet {

    private MessageService messageService = new MessageService();
    UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String senderIdStr = request.getParameter("senderId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        if (senderIdStr == null || senderIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing senderId.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            int senderId = Integer.parseInt(senderIdStr);
            List<Object> resp = new ArrayList<>();
            List<MessageDTO> messages = messageService.getAllMessagesBySender(userId, senderId);
            String username = userService.getUsernameByUserId(senderId);
//            response.getWriter().write(objectMapper.writeValueAsString(username));
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("messages", messages);
            responseData.put("receiverId", senderId);
            response.setContentType("application/json");
            resp.add(username);
            resp.add(responseData);
            response.getWriter().write(objectMapper.writeValueAsString(resp));

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve messages.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}