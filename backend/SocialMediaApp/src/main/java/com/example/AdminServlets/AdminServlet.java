package com.example.AdminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Bean.Post;
import org.example.Bean.User;
import org.example.DAO.Admin.AdminException;
import org.example.service.AdminService;
import org.example.service.PostService;
import org.example.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/*")
public class AdminServlet extends HttpServlet {
    private UserService userService;
    private PostService postService;
    private AdminService adminService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        postService = new PostService();
        adminService = new AdminService();
        objectMapper = new ObjectMapper();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.print("YR");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdminLoggedIn(request, response)) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Should be more specific if no action
            return;
        }

        try {
            switch (pathInfo) {
                case "/users":
                    List<User> users = userService.getAllUser();
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("users", users)));
                    break;
                case "/posts":
                    List<Post> posts = postService.getAllPosts();
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("posts", posts)));
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Operation failed: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdminLoggedIn(request, response)) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            switch (pathInfo) {
                case "/createAdmin":
                    handleCreateAdmin(request, response);
                    break;
                case "/deleteUser":
                    handleDeleteUser(request, response);
                    break;
                case "/banUser":
                    handleBanUser(request, response);
                    break;
                case "/unbanUser":
                    handleUnbanUser(request, response);
                    break;
                case "/deletePost":
                    handleDeletePost(request, response);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Operation failed: " + e.getMessage())));
        } catch (AdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void handleCreateAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, AdminException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean success = adminService.addAdmin(username, password);

        Map<String, Object> createAdminResponse = new HashMap<>();
        createAdminResponse.put("success", success);
        createAdminResponse.put("message", success ? "New admin created successfully!" : "Failed to create admin. Username may already exist.");

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createAdminResponse));
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Missing userId parameter.")));
            return;
        }
        try {
            int userId = Integer.parseInt(userIdParam);
            userService.deleteUser(userId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "User deleted successfully.")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Invalid userId format.")));
        }
    }

    private void handleBanUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Missing userId parameter.")));
            return;
        }
        try {
            int userId = Integer.parseInt(userIdParam);
            userService.banUser(userId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "User banned successfully.")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Invalid userId format.")));
        }
    }

    private void handleUnbanUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Missing userId parameter.")));
            return;
        }
        try {
            int userId = Integer.parseInt(userIdParam);
            userService.unbanUser(userId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "User unbanned successfully.")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Invalid userId format.")));
        }
    }

    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String postIdParam = request.getParameter("postId");
        if (postIdParam == null || postIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Missing postId parameter.")));
            return;
        }
        try {
            int postId = Integer.parseInt(postIdParam);
            postService.deletePost(postId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Post deleted successfully.")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(java.util.Collections.singletonMap("message", "Invalid postId format.")));
        }
    }
}