package com.ufide.biblioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesoController {

    @GetMapping("/403")
    public String accesoDenegado() {
        return "403";
    }
}