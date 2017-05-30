package com.gruppe4b.edivator.backend.controller;

import com.gruppe4b.edivator.backend.domain.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {


    @RequestMapping("/helloworld")
    public String helloworld() {
        return "{\"id\":1,\"content\":\"Hello, World!\"}";
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(1,
                String.format("yyyxxxHello, %s!", name));
    }
}