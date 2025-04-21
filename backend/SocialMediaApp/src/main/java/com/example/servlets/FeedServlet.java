package com.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.DTO.PostDTO;
import org.example.Bean.Post;
import org.example.Bean.User;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Likes.LikeException;
import org.example.service.CommentService;
import org.example.service.FollowerService;
import org.example.service.LikeService;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/feed") // Updated endpoint for API
public class FeedServlet extends HttpServlet {
    private UserService userService;
    private PostService postService;
    private FollowerService followerService;
    private LikeService likeService;
    private CommentService commentService;
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

    public void init() {
        userService = new UserService();
        postService = new PostService();
        followerService = new FollowerService();
        likeService = new LikeService();
        commentService = new CommentService();
    }

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

        String username = (String) session.getAttribute("username");
        List<Post> feedPosts = new ArrayList<>();
        int userId=0;
        try {
           userId = userService.getUser(username).getUserId();
            List<User> following = followerService.seeFollowing(userId);

            if (following == null || following.isEmpty()) {
                feedPosts = likeService.mostLikedPosts();
            } else {
                feedPosts = postService.viewFeed(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not retrieve feed.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        List<PostDTO> viewPosts = new ArrayList<>();
        if (feedPosts != null && !feedPosts.isEmpty()) {
            for (Post post : feedPosts) {
                try {
                    PostDTO dto = new PostDTO();
                    dto.setPostId(post.getPostId());
                    dto.setPostType(post.getPostType());
                    dto.setCaption(post.getPostCaption());
                    dto.setDateTime(post.getCreatedAt());
                    dto.setUserId(post.getUserId());
                    dto.setUsername(userService.getUser(post.getUserId()).getUsername());
                    dto.setLikeCount(likeService.likeCount(post.getPostId()));
                    dto.setComments(commentService.getCommentsDTO(post.getPostId()));
                    dto.setMediaPath(post.getFilePath());
                    dto.setLiked(likeService.hasLiked(post.getPostId(), userId));
                    viewPosts.add(dto);
                } catch (SQLException | CommentException | LikeException e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Could not retrieve feed.");
                    resp.setContentType("application/json");
                    resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return;
                }
            }
        }

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(viewPosts));
    }

    public static String getTimeAgo(Timestamp postTime) {
        LocalDateTime postDateTime = postTime.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(postDateTime, now);

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + " days ago";
        } else {
            return postDateTime.toLocalDate().toString();
        }
    }
}