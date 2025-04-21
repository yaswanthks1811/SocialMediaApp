package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Comments.CommentException;
import org.example.service.CommentService;
import org.example.service.NotificationService;
import org.example.service.PostService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/addComment") // Updated endpoint for API
public class AddCommentServlet extends HttpServlet {
    private NotificationService notificationService = new NotificationService();
    private PostService postService = new PostService();
    private CommentService commentService = new CommentService(); // Initialize CommentService
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String postIdStr = request.getParameter("postId");
        String commentText = request.getParameter("commentText");

        if (postIdStr == null || postIdStr.isEmpty() || commentText == null || commentText.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing post ID or comment text.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        int postId = Integer.parseInt(postIdStr);

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
            commentService.commentPost(userId, postId, commentText);

            int recepId = postService.getPostById(postId).getUserId();
            notificationService.createNotification(recepId, userId, postId, "comment");

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Comment added successfully.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(successResponse));

        } catch (CommentException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not add comment.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }
    }
}