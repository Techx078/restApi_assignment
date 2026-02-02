package com.example.demo.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        HttpServletResponse response) {
        Cookie cookie = new Cookie("DUMMY_TOKEN", "12345");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged in with dummy token");
    }
    @GetMapping("/get-cookies")
    public ResponseEntity<Map<String, String>> getCookies(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return ResponseEntity.ok(cookieMap);
    }
}
