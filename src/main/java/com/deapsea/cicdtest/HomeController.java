package com.deapsea.cicdtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @Value("${kakao.apiKey}")
    private String apiKey;


    @GetMapping("/")
    public String index() {
        log.info("kakao.apiKey={}", apiKey);
        return "index";
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("TEST~~~~~~", HttpStatus.OK);
    }
}
