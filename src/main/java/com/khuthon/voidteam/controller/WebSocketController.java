package com.khuthon.voidteam.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

    @GetMapping("/api/Videochat")
    public String getRoom(Model model){
        return "videochat";
    }
}
