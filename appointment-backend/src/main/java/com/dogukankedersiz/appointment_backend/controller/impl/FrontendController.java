package com.dogukankedersiz.appointment_backend.controller.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping(value = {"/", "/login", "/register", "/appointment"})
    public String forwardToReact() {
        return "forward:/index.html";
    }
}