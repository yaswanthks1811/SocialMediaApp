package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.User;
import org.example.service.FollowerService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/viewFollowers")
public class ViewFollowersServlet extends HttpServlet {

    private FollowerService followerService = new FollowerService();
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not logged in.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        Integer currentUserId = (Integer) session.getAttribute("userId");
        String idParam = req.getParameter("id");
        int profileUserId = (idParam != null) ? Integer.parseInt(idParam) : currentUserId;

        try {
            List<User> followers = followerService.seeFollowers(profileUserId);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("users", followers);
            responseData.put("type", "Followers");
            responseData.put("profileUserId", profileUserId);
            responseData.put("currentUserId", currentUserId);

            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(responseData));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve followers.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}