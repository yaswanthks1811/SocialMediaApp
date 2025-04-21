package com.example.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Followers.FollowerException;
import org.example.service.FollowerService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/unfollow")
public class UnfollowServlet extends HttpServlet {

    private FollowerService followerService = new FollowerService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	Integer currentUserId = (Integer) req.getSession().getAttribute("userId");

        if (currentUserId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        String targetUserIdStr = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());
            targetUserIdStr = jsonNode.get("targetUserId").asText();

            if (targetUserIdStr == null || targetUserIdStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Missing target user ID in request body.");
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            int targetUserId = Integer.parseInt(targetUserIdStr);
            followerService.unFollowUser(currentUserId, targetUserId);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "User unfollowed successfully.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(successResponse));

        } catch (FollowerException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not follow user.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid JSON request body or missing targetUserId.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }
}