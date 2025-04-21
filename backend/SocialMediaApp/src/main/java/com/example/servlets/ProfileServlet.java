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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/profile") // Updated endpoint for API
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    private PostService postService = new PostService();
    private LikeService likeService = new LikeService();
    private CommentService commentService = new CommentService();
    private FollowerService followerService = new FollowerService();
    private ObjectMapper objectMapper = new ObjectMapper(); // Initialize Jackson

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
            User user = userService.getUser(profileUserId);
            List<Post> userPosts = postService.getUserPostByUserId(profileUserId);
            List<PostDTO> viewPosts = new ArrayList<>();

            for (Post post : userPosts) {
            	 PostDTO dto = new PostDTO();
                 dto.setPostId(post.getPostId());
                 dto.setPostType(post.getPostType());
                 dto.setUserId(post.getUserId());
                 dto.setCaption(post.getPostCaption());
                 dto.setDateTime(post.getCreatedAt());
                 dto.setUsername(userService.getUser(post.getUserId()).getUsername());
                 dto.setLikeCount(likeService.likeCount(post.getPostId()));
                 dto.setComments(commentService.getCommentsDTO(post.getPostId()));
                 dto.setMediaPath(post.getFilePath());
                 dto.setLiked(likeService.hasLiked(post.getPostId(), profileUserId));
                 viewPosts.add(dto);
            }

            int followers = followerService.followerCount(profileUserId);
            int following = followerService.followingCount(profileUserId);
            boolean isFollowing = false;

            if (currentUserId != profileUserId) {
                isFollowing = followerService.isFollowing(profileUserId, currentUserId);
            }

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("user", user);
            profileData.put("posts", viewPosts);
            profileData.put("followers", followers);
            profileData.put("following", following);
            profileData.put("isOwner", currentUserId == profileUserId);
            profileData.put("isFollowing", isFollowing);

            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(profileData));

        } catch (SQLException | CommentException | LikeException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not retrieve profile.");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}