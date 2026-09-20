package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Hii controller inashughulikia "clean URLs" za kurasa za static HTML
 * (bila kuandika .html). Kila GetMapping inaelekeza (redirect) kwenda
 * faili halisi lililopo ndani ya src/main/resources/static/.
 *
 * Mfano: mtu akifungua https://xxx.up.railway.app/welcome
 * ataelekezwa kiotomatiki kwenda /welcome.html
 */
@RestController
public class PageController {

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        return redirectTo("/login.html");
    }

    @GetMapping("/register")
    public ResponseEntity<Void> register() {
        return redirectTo("/register.html");
    }

    @GetMapping("/welcome")
    public ResponseEntity<Void> welcome() {
        return redirectTo("/welcome.html");
    }

    @GetMapping("/report")
    public ResponseEntity<Void> report() {
        return redirectTo("/report.html");
    }

    @GetMapping("/reports")
    public ResponseEntity<Void> reports() {
        return redirectTo("/reports.html");
    }

    private ResponseEntity<Void> redirectTo(String location) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(location))
                .build();
    }
}

