package dev.rokong.main;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hong")
public class MainController {
    static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired MainService mainService;

    @RequestMapping("/rok")
    public String helloSpring(Model model){
        model.addAttribute("message", "hello spring! restart by code-server");
        model.addAttribute("currentDate", mainService.currentDate());

        return "/mainPage";
    }
}