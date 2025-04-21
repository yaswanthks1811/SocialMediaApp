package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DAO.Followers.FollowerException;
import org.example.service.FollowerService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/toggleFollow")
public class ToggleFollowServlet extends HttpServlet {

    private FollowerService followerService = new FollowerService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer currentUserId = (Integer) req.getSession().getAttribute("userId");
        String targetUserIdStr = req.getParameter("targetUserId");
        String action = req.getParameter("action");

        if (currentUserId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        if (targetUserIdStr == null || targetUserIdStr.isEmpty() || action == null || action.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing parameters.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            int targetUserId = Integer.parseInt(targetUserIdStr);

            if ("follow".equals(action)) {
                followerService.followUser(currentUserId, targetUserId);
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "User followed successfully.");
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(successResponse));
            } else if ("unfollow".equals(action)) {
                followerService.unFollowUser(currentUserId, targetUserId);
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "User unfollowed successfully.");
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(successResponse));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid action.");
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }

        } catch (FollowerException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not toggle follow.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}