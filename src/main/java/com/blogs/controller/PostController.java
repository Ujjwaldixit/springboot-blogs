package com.blogs.controller;

import com.blogs.model.*;
import com.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String homePage(@RequestParam(value = "start", defaultValue = "0") int pageNo,
                           @RequestParam(value = "limit", defaultValue = "10") int pageSize,
                           @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
                           @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
                           @RequestParam(value = "search", required = false) String searchKeyword,
                           @RequestParam(value = "authorId", required = false) List<Integer> authorIds,
                           @RequestParam(value = "tagId", required = false) List<Integer> tagsIds,
                           @RequestParam(value = "publishedAt", required = false) List<String> publishedDateTimes,
                           Model model) throws Exception {

        Page<Post> sortedAndPaginatedPosts = postService.findPostsWithPaginationAndSorting(
                pageNo, pageSize, sortField, sortOrder);

        List<Post> posts = sortedAndPaginatedPosts.toList();

        if (searchKeyword != null) {
            sortedAndPaginatedPosts = null;
            posts = postService.findPostsByKeyword(searchKeyword);

            if (posts == null) {
                List<Tag> tags = tagService.findTagsByName(List.of(searchKeyword));

                if (tags != null) {
                    List<PostTag> postTags = postTagService.findPostTagsByTags(tags);

                    posts = postService.findPostsByPostTag(postTags);
                }
            }
        }

        if (authorIds != null || tagsIds != null || publishedDateTimes != null) {
            sortedAndPaginatedPosts = null;
            posts = new ArrayList<>();

            if (authorIds != null) {
                posts.addAll(postService.findPostsByAuthorId(authorIds));
            }

            if (publishedDateTimes != null) {
                for (String publishedDateTime : publishedDateTimes) {
                    if (publishedDateTime.length() == 10) {
                        posts.addAll(postService.findPostByPublishedDate(publishedDateTime));
                    }
                    if (publishedDateTime.length() == 5) {
                        posts.addAll(postService.findPostByPublishedTime(publishedDateTime));
                    } else {
                        posts.addAll(postService.findPostByPublishedDateTime(Timestamp.valueOf(publishedDateTime)));
                    }
                }
            }

            if (tagsIds != null) {
                List<Tag> tags = tagService.findTagsByIds(tagsIds);

                if (tags != null) {
                    List<PostTag> postTags = postTagService.findPostTagsByTags(tags);
                    posts.addAll(postService.findPostsByPostTag(postTags));
                }
            }
        }

        if (sortedAndPaginatedPosts != null)
            model.addAttribute("totalPages", sortedAndPaginatedPosts.getTotalPages());

        model.addAttribute("posts", posts);
        model.addAttribute("start", pageNo);
        model.addAttribute("limit", pageSize);
        model.addAttribute("keyword", searchKeyword);
        return "index";
    }

    @GetMapping("/newPost")
    public String newPost(@AuthenticationPrincipal UserDetailsImpl user,
                          Model model,
                          Post post) {

        System.out.println("newpost1");
        post.setAuthor(user.getName());
        System.out.println("newpost2"+user.getName());
        List<Tag> tags = tagService.getAllTags();
        System.out.println("newpost3"+tags);
        model.addAttribute("tags", tags);
        model.addAttribute("post", post);

        return "newPost";
    }

    @PostMapping("/savePost")
    public String savePost(@AuthenticationPrincipal UserDetailsImpl user,
                           @ModelAttribute("post") Post post,
                           @RequestParam("Tags") String tags,
                           PostTag postTag) {

        post.setAuthor(user.getName());
        post.setAuthorId(user.getUserId());
        post = postService.savePost(post);

        int postId = post.getId();

        List<Integer> tagIds = new ArrayList<>();

        if (tags.length() > 0) {
            tagIds = tagService.saveTag(tags);
        }

        postTag.setPostId(postId);

        if (tagIds.size() > 0) {
            for (int tagId : tagIds) {
                postTag.setTagId(tagId);
                postTagService.savePostTag(postTag);
            }
        } else {
            postTag.setTagId(0);
            postTagService.savePostTag(postTag);
        }

        return "redirect:/";
    }

    @GetMapping("/fullPost/{postId}")
    public String displayFullPost(@AuthenticationPrincipal UserDetailsImpl user,
                                  @PathVariable("postId") int postId,
                                  Model model) {

        Post post = postService.findPostById(postId);

        List<Comment> comments = commentService.findCommentsByPostId(postId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("user", user);

        if (user != null)
            model.addAttribute("userId", user.getUserId());

        return "fullPost";
    }

    @GetMapping("/updatePost/{postId}")
    public String updatePost(@AuthenticationPrincipal UserDetailsImpl user,
                             @PathVariable("postId") int id,
                             Model model) {

        Post post = postService.findPostById(id);

        if (user.getUserId() != post.getAuthorId() || !user.getRole().equals("ADMIN"))
            throw new UsernameNotFoundException("Not Intended User");

        List<Tag> tags = tagService.getAllTags();

        List<PostTag> postTags = postTagService.findPostTagsByPostId(id);

        postTagService.deletePostTags(postTags);

        model.addAttribute("post", post);
        model.addAttribute("tags", tags);

        return "newPost";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@AuthenticationPrincipal UserDetailsImpl user,
                             @PathVariable("postId") int postId) {

        Post post = postService.findPostById(postId);

        if (user.getUserId() != post.getAuthorId() || !user.getRole().equals("ADMIN"))
            throw new UsernameNotFoundException("Not Intended User");

        postService.deletePost(postId);
        postTagService.deletePostTags(postTagService.findPostTagsByPostId(postId));
        commentService.deleteCommentByPostId(postId);

        return "redirect:/";
    }

    @GetMapping("/addComment/{postId}")
    public String addComment(@PathVariable("postId") int postId,
                             Model model,
                             Comment comment) {

        model.addAttribute("_comment", comment);
        model.addAttribute("postId", postId);

        return "commentForm";
    }

    @PostMapping("/saveComment")
    public String saveComment(@ModelAttribute("_comment") Comment comment) {
        commentService.saveComment(comment);

        return "redirect:/fullPost/" + comment.getPostId();
    }

    @GetMapping("/updateComment/{commentId}/{postId}")
    public String updateComment(@PathVariable("commentId") int commentId, Model model) {
        Comment comment = commentService.findCommentById(commentId);

        model.addAttribute("_comment", comment);

        return "commentForm";
    }

    @GetMapping("/deleteComment/{commentId}/{postId}")
    public String deleteComment(@PathVariable("commentId") int commentId,
                                @PathVariable("postId") int postID) {

        commentService.deleteComment(commentId);

        return "redirect:/fullPost/" + postID;
    }
}