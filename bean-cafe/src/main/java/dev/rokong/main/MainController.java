package dev.rokong.main;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping("/")
@Api(tags={"Test Status"})
public class MainController {

    @Autowired
    private MainService mainService;

    @RequestMapping(value="test/database", method= RequestMethod.GET)
    @ApiOperation(value = "database test page")
    public String helloSpring(Model model){
        model.addAttribute("message", "hello spring! restart by code-server");
        model.addAttribute("currentDate", mainService.currentDate());

        return "/mainPage";
    }

    @RequestMapping(value="test/server", method=RequestMethod.GET)
    @ApiOperation(value="server status test page")
    public String serverStatus(){
        return "redirect:/status.jsp";
    }
}