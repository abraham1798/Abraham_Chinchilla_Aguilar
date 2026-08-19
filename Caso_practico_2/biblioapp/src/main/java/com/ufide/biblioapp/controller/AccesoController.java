package com.ufide.biblioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccesoController {

    @RequestMapping("/403")
    public String accesoDenegado() {
        return "403";
    }
}