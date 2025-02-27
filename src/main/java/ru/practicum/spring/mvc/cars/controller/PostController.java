package ru.practicum.spring.mvc.cars.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.spring.mvc.cars.dto.CommentDto;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.service.CommentService;
import ru.practicum.spring.mvc.cars.service.PostService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/details")
    public String showPostDetails(@RequestParam("postId") Long theId, Model theModel) {
        PostDto post = postService.findById(theId);
        List<CommentDto> comments = commentService.findAllByPostId(theId);
        theModel.addAttribute("post", post);
        theModel.addAttribute("comments", comments);
        return "posts/post-details";
    }

    @GetMapping("/list")
    public String listPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "tag", required = false) String tag,
                            final Model theModel) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PostDto> postPage;
        if (tag != null && !tag.isEmpty()) {
            postPage = postService.findByTag(tag, pageable);
        } else {
            postPage = postService.findAll(pageable);
        }

        theModel.addAttribute("posts", postPage.getContent());
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", postPage.getTotalPages());
        theModel.addAttribute("tag", tag);
        return "posts/list-posts";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        PostDto post = new PostDto();
        post.setLikeCount(0L); // by default
        theModel.addAttribute("post", post);
        return "posts/post-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("postId") Long theId, Model theModel) {
        PostDto post = postService.findById(theId);
        theModel.addAttribute("post", post);
        return "posts/post-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("postId") Long theId) {
        commentService.deleteCommentsByPostId(theId);
        postService.deletePost(theId);

        return "redirect:/posts/list";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") PostDto postDto) {
        if (postDto.getId() == null) {
            postService.addPost(postDto);
        } else {
            postService.updatePost(postDto);
        }
        return "redirect:/posts/list";
    }

    @PostMapping(value = "/{postId}/like", produces = "application/json")
    @ResponseBody
    public Map<String, Object> likePost(@PathVariable("postId") Long postId) {
        PostDto updatedPost = postService.incrementLikeCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", updatedPost.getLikeCount());

        return response;
    }

    @PostMapping("/addComment")
    @ResponseBody
    public CommentDto addComment(@RequestBody CommentDto commentDto) {
        CommentDto savedCommentDto = commentService.addComment(commentDto); // to get new comment ID
        return savedCommentDto;
    }

    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId, @RequestParam("postId") Long postId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/details?postId=" + postId;
    }

    @PostMapping("/updateComment")
    @ResponseBody
    public CommentDto updateComment(@RequestBody CommentDto commentDto) {
        commentService.updateComment(commentDto);
        CommentDto updatedComment = commentService.findById(commentDto.getId());
        return updatedComment;
    }

}
