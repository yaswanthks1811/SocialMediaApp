package com.example.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.PostService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/deletePost")
public class DeletePostServlet extends HttpServlet {
    private PostService postService = new PostService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Map<String, String> responseMap = new HashMap<>();
        
        // Check authentication
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseMap.put("error", "User not logged in.");
            response.getWriter().write(objectMapper.writeValueAsString(responseMap));
            return;
        }

        // Parse request body
        StringBuilder requestBody = new StringBuilder();
        String line="";
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        String postId;
        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());
            postId = jsonNode.has("targetPostId") ? jsonNode.get("targetPostId").asText() : "";
            
            if (postId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("error", "Missing post ID.");
                response.getWriter().write(objectMapper.writeValueAsString(responseMap));
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("error", "Invalid JSON request body.");
            response.getWriter().write(objectMapper.writeValueAsString(responseMap));
            return;
        }

        try {
            int post_id = Integer.parseInt(postId);

            boolean deleted = postService.deletePost(post_id);
            
            if (deleted) {
                responseMap.put("message", "Post deleted successfully.");
                response.getWriter().write(objectMapper.writeValueAsString(responseMap));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMap.put("error", "Post not found or could not be deleted.");
                response.getWriter().write(objectMapper.writeValueAsString(responseMap));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMap.put("error", "Invalid post ID format.");
            response.getWriter().write(objectMapper.writeValueAsString(responseMap));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("error", "Database error occurred.");
            response.getWriter().write(objectMapper.writeValueAsString(responseMap));
        }
    }
}