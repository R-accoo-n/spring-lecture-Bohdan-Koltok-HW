package co.inventorsoft.academy.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class MainController {
    @ResponseBody
    @GetMapping()
    public String hello(){
        return "hello world";
    }
}
