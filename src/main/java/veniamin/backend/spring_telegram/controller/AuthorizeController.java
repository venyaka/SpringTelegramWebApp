package veniamin.backend.spring_telegram.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import veniamin.backend.spring_telegram.constant.PathConstants;
import veniamin.backend.spring_telegram.dto.request.TelegramAuthorizeReqDTO;
import veniamin.backend.spring_telegram.service.AuthorizeService;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.AUTHORIZE_CONTROLLER_PATH)
public class AuthorizeController {

    private final AuthorizeService authorizeService;

    @Value("${ngrok.url}")
    private String ngrokUrl;


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("ngrok_url", ngrokUrl);
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody TelegramAuthorizeReqDTO authorizeReqDTO, HttpServletRequest http) {
        authorizeService.authorizeUserByTelegram(authorizeReqDTO, http);
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/");
        return ResponseEntity.ok(response);
    }
}
