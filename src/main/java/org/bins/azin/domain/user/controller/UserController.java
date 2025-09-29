package org.bins.azin.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    /**
     * 로그인 화면 호출
     */
    @GetMapping("/")
    public String index() {
        return "pages/index";
    }

    /**
     * 회원가입 화면 호출
     */
    @GetMapping("/sign-up")
    public String signUp() {
        return "pages/user/signup";
    }
}