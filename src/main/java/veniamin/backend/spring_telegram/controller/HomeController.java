package veniamin.backend.spring_telegram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import veniamin.backend.spring_telegram.constant.PathConstants;
import veniamin.backend.spring_telegram.service.UserService;
import veniamin.backend.spring_telegram.util.NgrokFetcher;


@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.HOME_CONTROLLER_PATH)
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String getMe(Model model) {
        model.addAttribute(userService.getCurrentUserInfo());
        return "home";
    }
}
