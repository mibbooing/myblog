package com.example.myblog.controller;

import com.example.myblog.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;


    @GetMapping("/")
    public String index(Model model) {
        Long topicId = homeService.getDefaultTopicId();
        model.addAttribute("homePostPreviewDtoList", homeService.getHomeInfo(0, 10,topicId));
        model.addAttribute("topicList", homeService.getTopicList());
        model.addAttribute("selectedTopic", topicId);
        return "home";
    }


    @GetMapping("/home")
    public String home(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "topicId") Long topicId) {
        model.addAttribute("homePostPreviewDtoList", homeService.getHomeInfo(page, size, topicId));
        model.addAttribute("topicList", homeService.getTopicList());
        model.addAttribute("selectedTopic", topicId);
        return "home";
    }

}
