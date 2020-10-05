package dev.rokong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hong")
public class MainController {
    static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/rok")
    public String helloSpring(Model model){
        model.addAttribute("message", "hello spring! after change request mapping");
        return "/mainPage";
    }
}