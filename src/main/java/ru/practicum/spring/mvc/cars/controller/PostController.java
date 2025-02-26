package ru.practicum.spring.mvc.cars.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/details")
    public String showPostDetails(@RequestParam("postId") Long theId, Model theModel) {
        PostDto post = postService.findById(theId);
        theModel.addAttribute("post", post);
        return "posts/post-details";
    }

    @GetMapping("/list")
    public String listPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            final Model theModel) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> postPage = postService.findAll(pageable);
        theModel.addAttribute("posts", postPage.getContent());
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", postPage.getTotalPages());
        return "posts/list-posts";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        PostDto post = new PostDto();
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
}
