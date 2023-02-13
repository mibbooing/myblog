package com.example.myblog.controller;

import com.example.myblog.dto.CommentDto;
import com.example.myblog.dto.CommentFormDto;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/comments")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final BlogService blogService;

    @PostMapping("/new")
    public String newComment(HttpServletRequest request, Principal principal, @Valid CommentFormDto commentFormDto, BindingResult bindingResult, Model model, RedirectAttributes rttr) {
        if (bindingResult.hasErrors()) {
            rttr.addFlashAttribute("bindingResult", bindingResult);
            rttr.addFlashAttribute("commentFormDto", commentFormDto);
            return "redirect:" + request.getHeader("Referer");
        }
        try {
            commentService.createComment(commentFormDto, principal.getName());
        } catch (Exception e) {
            System.out.println("에러");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @PatchMapping("/{commentNum}")
    @PreAuthorize("@authorizationChecker.checkCommentAuth(#commentNum, principal.username)")
    public String updateComment(@PathVariable("commentNum") Long commentNum, HttpServletRequest request, @Valid CommentDto commentDto, BindingResult bindingResult, Principal principal, Model model, RedirectAttributes rttr) {
        if (bindingResult.hasErrors()) {
            rttr.addFlashAttribute("bindingResult", bindingResult);
            rttr.addFlashAttribute("commentDto", commentDto);
            return "redirect:" + request.getHeader("Referer");
        }
        try {
            commentService.updateComment(commentDto, principal.getName());
        } catch (Exception e) {
            System.out.println("에러");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @DeleteMapping("/{commentNum}")
    @PreAuthorize("@authorizationChecker.checkCommentAuth(#commentNum, principal.username)")
    public String deleteComment(@PathVariable("commentNum") Long commentNum, HttpServletRequest request, CommentDto commentDto, Principal principal) {
        System.out.println("Patch Comment");
        try {
            commentService.deleteComment(commentDto, principal.getName());
        } catch (Exception e) {
            System.out.println("에러");
        }
        return "redirect:" + request.getHeader("Referer");
    }

}
