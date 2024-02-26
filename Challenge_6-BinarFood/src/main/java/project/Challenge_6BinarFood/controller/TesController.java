package project.Challenge_6BinarFood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tes")
public class TesController {
    public String tes(){
        return "Running";
    }
}
