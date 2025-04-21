package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Likes.LikeException;
import org.example.service.LikeService;
import org.example.service.NotificationService;
import org.example.service.PostService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/likePost") // Updated endpoint for API
public class LikePostServlet extends HttpServlet {
    private LikeService likeService = new LikeService();
    private NotificationService notificationService = new NotificationService();
    private PostService postService = new PostService();
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.print(postId);

        if (userId == 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        if (postId == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing post ID.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {

            if (likeService.hasLiked(postId, userId)) {
                likeService.removeLike(userId, postId);
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Like removed.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(successResponse));
            } else {
                likeService.likePost(userId, postId);
                int recepId = postService.getPostById(postId).getUserId();
                notificationService.createNotification(recepId, userId, postId, "like");
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Post liked.");
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(successResponse));
            }
        } catch (SQLException | LikeException | CommentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not like/unlike post.");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}