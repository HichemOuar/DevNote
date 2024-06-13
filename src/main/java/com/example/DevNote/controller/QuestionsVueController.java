package com.example.DevNote.controller;

import com.example.DevNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/vue")
@Controller
public class QuestionsVueController
{
    @Autowired
    private UserService userService;

    @GetMapping("/questionboard")
    public String questionboard() {
        return "questionboard";
    }

    @GetMapping("/searchquestion")
    public String searchquestion() {
        return "searchquestion";
    }

    @GetMapping("/createquestion")
    public String createquestion() {
        return "createquestion";
    }

}