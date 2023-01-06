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

    @ExceptionHandler(value = NullPointerException.class)
    public String handleAll(NullPointerException e, Model model, HttpServletRequest request){
        if(e.getMessage().equals("권한검색실패")) {
            model.addAttribute("errorMessage", "해당 페이지에 대한 접근권한이 없습니다.");
            model.addAttribute("url", "/");
        }
        if(request.getHeader("Referer") == null){
            model.addAttribute("url", "/");
        }
        return "/error/error";
    }
}
