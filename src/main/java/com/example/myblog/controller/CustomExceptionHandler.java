package com.example.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String handleAll(Exception e, Model model, HttpServletRequest request) {
        if (e.getMessage().equals("ERR_BLOG_PERMISSION")) {
            model.addAttribute("errorMessage", "잘못된 접근입니다.");
        } else if(e.getMessage().equals("ERR_BLOG_NAME")){
            model.addAttribute("errorMessage", "해당 블로그를 찾을 수 없습니다.");
        }else if (e.getMessage().equals("ERR_POST_NUM")) {
            model.addAttribute("errorMessage", "존재하지 않는 게시물입니다.");
        } else if (e.getMessage().equals("ERR_POST_PERMISSION_USER")) {
            model.addAttribute("errorMessage", "이웃에게만 공개된 게시물입니다.");
        } else if (e.getMessage().equals("ERR_POST_PERMISSION_ADMIN")) {
            model.addAttribute("errorMessage", "해당 블로그 관리자만 접근 가능합니다.");
        } else if (e.getMessage().equals("ERR_POST_ANONYMOUS")) {
            model.addAttribute("errorMessage", "로그인이 필요한 게시물입니다.");
            model.addAttribute("url", "/members/login");
        } else if (e.getMessage().equals("ERR_COMMENT_NOT_FOUND")){
            model.addAttribute("errorMessage", "요청한 댓글을 찾을 수 없습니다.");
        }
        else{
            model.addAttribute("errorMessage", "알수없는 오류가 발생하였습니다.");
        }
        if(model.getAttribute("url") == null) {
            if (request.getHeader("Referer") == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", request.getHeader("Referer"));
            }
        }
        return "error/error";
    }

}
